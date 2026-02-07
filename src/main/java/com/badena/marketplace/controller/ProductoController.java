package com.badena.marketplace.controller;

import com.badena.marketplace.dto.ProductoDetalleDTO;
import com.badena.marketplace.dto.ProductoRegistroDTO;
import com.badena.marketplace.dto.ImagenDetalleDTO;
import com.badena.marketplace.entity.Categoria;
import com.badena.marketplace.entity.ImagenProducto;
import com.badena.marketplace.entity.Marca;
import com.badena.marketplace.entity.Producto;
import com.badena.marketplace.entity.Tienda;
import com.badena.marketplace.repository.CategoriaRepository;
import com.badena.marketplace.repository.ImagenProductoRepository;
import com.badena.marketplace.repository.MarcaRepository;
import com.badena.marketplace.repository.ProductoRepository;
import com.badena.marketplace.repository.TiendaRepository;
import com.badena.marketplace.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*") 
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private TiendaRepository tiendaRepository; 

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ImagenProductoRepository imagenProductoRepository;

    @Autowired
    private ProductService productService;

    
    // mostrar todos los productos (Catalogo publico).
    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    // mostrar por tienda, perfil (Dashboard).
    @GetMapping("/tienda/{tiendaId}")
    public ResponseEntity<List<Producto>> listarPorTienda(@PathVariable Long tiendaId) {
        List<Producto> productos = productoRepository.findByTiendaId(tiendaId);
        
        return ResponseEntity.ok(productos);
    }

     // Llamar elos dellates por EAN.
   @GetMapping("/{codigoUniversal}")
    public ResponseEntity<?> obtenerProductoPorCodigo(@PathVariable String codigoUniversal) {
    // Busqueda del producto en la bd.
    java.util.Optional<Producto> productoOpt = productoRepository.findByCodigoUniversal(codigoUniversal);

    // Si no existe se devuelve el error 404 de producto no encontrado.
    if (productoOpt.isEmpty()) {
        return ResponseEntity.status(404).body(java.util.Map.of("error", "Producto no encontrado"));
    }

    // Si existe, se ubica y se crea su DTO.
    Producto producto = productoOpt.get();
    ProductoDetalleDTO dto = new ProductoDetalleDTO();
    
    // consulta de los datos basicos.
    dto.setNombre(producto.getNombreProducto());
    dto.setCodigoUniversal(producto.getCodigoUniversal());
    dto.setDescripcionMarketing(producto.getDescripcionMarketing());
    dto.setDescripcionTecnica(producto.getDescripcionTecnica());

    // asignar los camposde valores tecnicos (para que no salgan null)
    dto.setContenidoNeto(producto.getContenidoNeto());
    dto.setUnidadMedida(producto.getUnidadMedida());
    dto.setPesoKg(producto.getPesoKg());
    dto.setLargoCm(producto.getLargoCm());
    dto.setAnchoCm(producto.getAnchoCm());
    dto.setAltoCm(producto.getAltoCm());

    // Relaciones entre Marca y Tienda
    if (producto.getMarca() != null) {
        dto.setNombreMarca(producto.getMarca().getNombre());
        if (producto.getMarca().getTienda() != null) {
            dto.setNombreTienda(producto.getMarca().getTienda().getNombrePublico());
        }
    }
    
    if (producto.getCategoria() != null) {
        dto.setCategoria(producto.getCategoria().getNombre());
    }

    //lista de imagenes
    java.util.List<ImagenDetalleDTO> listaFotosDto = new java.util.ArrayList<>();
    for (ImagenProducto imgEntidad : producto.getImagenes()) {
        ImagenDetalleDTO imgDto = new ImagenDetalleDTO();
        imgDto.setUrlImagen(imgEntidad.getUrlImagen());
        imgDto.setEsPortada(imgEntidad.isEsPortada());
        listaFotosDto.add(imgDto);

        if (imgEntidad.isEsPortada()) {
            dto.setFotoPortada(imgEntidad.getUrlImagen());
        }
    }
    
    //si no hay foto de portada marcada, se usa la primera.
    if (dto.getFotoPortada() == null && !listaFotosDto.isEmpty()) {
        dto.setFotoPortada(listaFotosDto.get(0).getUrlImagen());
    }
    
        dto.setImagenes(listaFotosDto);

        return ResponseEntity.ok(dto);
}
    // crear product con hasta 5 fotos.
    @PostMapping(value = "/con-imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") ProductoRegistroDTO dto, 
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
    

    try {
        // Creacion de la entidad prodcto.
        Producto producto = new Producto();
        producto.setNombreProducto(dto.getNombre());
        producto.setSkuVendedor(dto.getSkuVendedor());
        producto.setCodigoUniversal(dto.getCodigoUniversal());
        producto.setDescripcionMarketing(dto.getDescripcionMarketing());
        producto.setDescripcionTecnica(dto.getDescripcionTecnica());
        producto.setVisible(true);
        producto.setContenidoNeto(dto.getContenidoNeto());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setPesoKg(dto.getPesoKg());
        producto.setLargoCm(dto.getLargoCm());
        producto.setAnchoCm(dto.getAnchoCm());
        producto.setAltoCm(dto.getAltoCm());

        // Generar el slug del product.
        String slugBase = dto.getNombre().toLowerCase().replaceAll("[^a-z0-9]", "-");
        producto.setSlug(slugBase + "-" + UUID.randomUUID().toString().substring(0, 5));

        //creacion de la relacion
        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + dto.getIdMarca()));
        producto.setMarca(marca);

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + dto.getIdCategoria()));
        producto.setCategoria(categoria);

        //ubicar la tienda
        if (dto.getIdTienda() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo idTienda es obligatorio y no llegó en la solicitud."));
        }

        Tienda tienda = tiendaRepository.findById(dto.getIdTienda())
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada con ID: " + dto.getIdTienda()));
        producto.setTienda(tienda);

        // guardar el nuevo product
        Producto productoGuardado = productoRepository.save(producto);

        // guardar las imagenes.
        if (imagenes != null && !imagenes.isEmpty()) {
            Path rootPath = Paths.get("uploads").toAbsolutePath();
            if (!Files.exists(rootPath)) Files.createDirectories(rootPath);

            for (int i = 0; i < Math.min(imagenes.size(), 5); i++) {
                MultipartFile archivo = imagenes.get(i);
                if (archivo.isEmpty()) continue;

                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Path targetPath = rootPath.resolve(nombreArchivo);
                Files.copy(archivo.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                ImagenProducto imagenEntity = new ImagenProducto();
                imagenEntity.setProducto(productoGuardado);
                imagenEntity.setUrlImagen("/uploads/" + nombreArchivo);
                imagenEntity.setEsPortada(i == 0); 
                imagenEntity.setOrden(i + 1);

                imagenProductoRepository.save(imagenEntity);
            }
        }

        return ResponseEntity.ok(Map.of(
            "mensaje", "Producto publicado exitosamente",
            "id", productoGuardado.getId()
        ));

    } catch (Exception e) {
        e.printStackTrace(); 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}

    //Para actualizar un product
    @PostMapping(value = "/actualizar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    @Transactional
    public ResponseEntity<?> actualizarProducto(
            @RequestPart("producto") ProductoRegistroDTO dto,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            // Ubicar eel producto existente por su EAN (Codigo universal)
            Producto producto = productoRepository.findByCodigoUniversal(dto.getCodigoUniversal())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con EAN: " + dto.getCodigoUniversal()));

            // Actualizacion del nombre, y todos los datos del producto
            producto.setNombreProducto(dto.getNombre());
            producto.setDescripcionMarketing(dto.getDescripcionMarketing());
            producto.setDescripcionTecnica(dto.getDescripcionTecnica());
            producto.setContenidoNeto(dto.getContenidoNeto());
            producto.setUnidadMedida(dto.getUnidadMedida());
            producto.setPesoKg(dto.getPesoKg());
            producto.setLargoCm(dto.getLargoCm());
            producto.setAnchoCm(dto.getAnchoCm());
            producto.setAltoCm(dto.getAltoCm());

        //Actualizacion de las relaciones
        Marca marca = marcaRepository.findById(dto.getIdMarca())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
        producto.setMarca(marca);

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        //Guardar cambios, save
        Producto productoActualizado = productoRepository.save(producto);

        // actualizacion de las imagenes (si se subieron nuevas)
        if (imagenes != null && !imagenes.isEmpty()) {
            imagenProductoRepository.deleteByProducto(productoActualizado);

            Path rootPath = Paths.get("uploads").toAbsolutePath();
            for (int i = 0; i < Math.min(imagenes.size(), 5); i++) {
                MultipartFile archivo = imagenes.get(i);
                if (archivo.isEmpty()) continue;

                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Path targetPath = rootPath.resolve(nombreArchivo);
                Files.copy(archivo.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                ImagenProducto imagenEntity = new ImagenProducto();
                imagenEntity.setProducto(productoActualizado);
                imagenEntity.setUrlImagen("/uploads/" + nombreArchivo);
                imagenEntity.setEsPortada(i == 0);
                imagenEntity.setOrden(i + 1);

                imagenProductoRepository.save(imagenEntity);
            }
        }

        return ResponseEntity.ok(Map.of("mensaje", "Producto actualizado correctamente"));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}

    @PostMapping("/guardar")
        public ResponseEntity<?> guardarProducto(@RequestBody Map<String, Object> payload) {
            try {
                Producto producto = new Producto();
                producto.setNombreProducto(payload.get("nombre").toString());
            
                // se ubica la marca
                Long idMarca = Long.parseLong(payload.get("idMarca").toString());
                Marca marca = marcaRepository.findById(idMarca)
                                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
                
                producto.setMarca(marca);

                producto.setTienda(marca.getTienda());

                productoRepository.save(producto);
                
                return ResponseEntity.ok(Map.of("mensaje", "Producto registrado correctamente en tu tienda"));

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Error al guardar: " + e.getMessage()));
            }
        }


         @GetMapping("/buscar")
            public ResponseEntity<List<Producto>> buscarProductos(@RequestParam("q") String consulta) {
                List<Producto> resultados = productService.buscarGeneral(consulta);
                return ResponseEntity.ok(resultados);
            }   



}