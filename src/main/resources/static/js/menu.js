document.addEventListener("DOMContentLoaded", function() {
    const sesion = JSON.parse(localStorage.getItem('sesionBadena'));
    const menuContainer = document.getElementById('app-menu');

    if (!menuContainer) return;

    // 1. SI NO HAY SESIÓN, MOSTRAR NAVBAR SIMPLE O REDIRIGIR
    if (!sesion) {
        menuContainer.innerHTML = '<div class="alert alert-warning m-3">No hay sesión iniciada</div>';
        return;
    }

    // 2. ESTILOS DEL SIDEBAR (Inyectados dinámicamente)
    const sidebarWidth = "260px";
    
    // Ajustar el cuerpo de la página para que el menú no tape el contenido
    document.body.style.marginLeft = sidebarWidth; 
    document.body.style.minHeight = "100vh";
    document.body.style.backgroundColor = "#f8f9fa"; // Gris suave de fondo

    // 3. CONSTRUCCIÓN HTML DEL SIDEBAR
    let htmlMenu = `
    <div class="d-flex flex-column flex-shrink-0 p-3 text-white bg-primary" 
         style="width: ${sidebarWidth}; height: 100vh; position: fixed; top: 0; left: 0; overflow-y: auto;">
        
        <a href="dashboard.html" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <span class="fs-4 fw-bold">Badena Panel</span>
        </a>
        <hr>
        
        <div class="mb-3">
            <small class="text-dark text-uppercase fw-bold " style="font-size: 0.75rem;">Usuario</small><br>
            <span class="fw-bold">${sesion.nombreUsuario}</span><br>
            <small class="text-warning">${sesion.nombreTienda || sesion.tipoUsuario}</small>
        </div>

        <ul class="nav nav-pills flex-column mb-auto">
    `;

    const rol = sesion.tipoUsuario;

    // --- OPCIONES PARA PUBLISHER (VENDEDOR) ---
    if (rol === 'PUBLISHER') {
        htmlMenu += `
            <li class="nav-item mb-1">
                <a href="dashboard.html" class="nav-link text-white" aria-current="page">
                    <i class="bi bi-speedometer2 me-2"></i> Dashboard
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="add_product.html" class="nav-link active bg-primary text-white">
                    <i class="bi bi-plus-circle me-2"></i> Nuevo Producto
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="mis_productos.html" class="nav-link text-white">
                    <i class="bi bi-box-seam me-2"></i> Mi Catálogo
                </a>
            </li>
            <li class="nav-item mb-1">
                <a href="ordenes.html" class="nav-link text-white">
                    <i class="bi bi-cart-check me-2"></i> Pedidos
                </a>
            </li>
        `;
    }

    // --- OPCIONES PARA ADMIN ---
    if (rol === 'ADMIN') {
        htmlMenu += `
            <li class="nav-item"><a href="admin_users.html" class="nav-link text-white"><i class="bi bi-people me-2"></i> Usuarios</a></li>
            <li class="nav-item"><a href="admin_tiendas.html" class="nav-link text-white"><i class="bi bi-shop me-2"></i> Tiendas</a></li>
            <li class="nav-item"><a href="market.html" class="nav-link text-white"><i class="bi bi-shop me-2"></i> Productos</a></li>
            <li class="nav-item"><a href="add_product.html" class="nav-link text-white"><i class="bi bi-shop me-2"></i> Agregar producto</a></li>
            `;
    }
   // --- OPCIONES PARA ADMIN ---
    if (rol === 'CORPORATION') {
        htmlMenu += `
            <li class="nav-item"><a href="admin_users.html" class="nav-link text-white"><i class="bi bi-people me-2"></i> Usuarios</a></li>
            <li class="nav-item"><a href="market.html" class="nav-link text-white"><i class="bi bi-shop me-2"></i> Productos</a></li>
            <li class="nav-item"><a href="add_product.html" class="nav-link text-white"><i class="bi bi-shop me-2"></i> Agregar producto</a></li>
        `;
    }


    // CIERRE DEL MENÚ
    htmlMenu += `
        </ul>
        <hr>
        <div class="dropdown">
            <button onclick="cerrarSesion()" class="btn btn-outline-danger w-100 d-flex align-items-center justify-content-center">
                <i class="bi bi-box-arrow-left me-2"></i> Cerrar Sesión
            </button>
        </div>
    </div>
    `;

    menuContainer.innerHTML = htmlMenu;
});

// Función de Cerrar Sesión
function cerrarSesion() {
    Swal.fire({
        title: '¿Salir del sistema?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, salir'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('sesionBadena');
            window.location.href = 'login.html';
        }
    });
}