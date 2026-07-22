document.addEventListener("DOMContentLoaded", async function() {
    const navbarPlaceholder = document.getElementById("navbar-placeholder");
    if (!navbarPlaceholder) return;

    // Leer qué tipo de vista necesita esta página (si no hay, por defecto es 'market')
    const tipoVista = navbarPlaceholder.getAttribute("data-tipo") || "market";

    try {
        const response = await fetch("navbar.html");
        const html = await response.text();
        navbarPlaceholder.innerHTML = html;

        // Pasamos el tipo de vista a la función
        actualizarMenuUsuario(tipoVista);
    } catch (error) {
        console.error("Error al cargar el navbar:", error);
    }
});

function actualizarMenuUsuario(tipoVista) {
    const usuarioJson = localStorage.getItem('usuario');
    const menuUsuario = document.getElementById('menuUsuario');
    
    // Elementos dinámicos del navbar
    const navBuscadorContainer = document.getElementById('navBuscadorContainer');
    const navTituloCentral = document.getElementById('navTituloCentral');
    const textoTituloCentral = document.getElementById('textoTituloCentral');
    
    if (!menuUsuario) return;

    if (usuarioJson) {
        const usuario = JSON.parse(usuarioJson);

        const nombreDisp = usuario.nombre || "Usuario";
        const tipoDisp = usuario.tipoUsuario || "MARKET";
        const tiendaDisp = usuario.nombreTienda || (usuario.tienda ? usuario.tienda.nombre : "Mi Tienda");
        const esCorporacion = tipoDisp === 'CORPORATION';
        const esPublicador = tipoDisp === 'PUBLISHER';

        // ==============================================
        // LÓGICA DE VISTAS (MARKET vs DASHBOARD)
        // ==============================================
        if (tipoVista === 'dashboard') {
            // Ocultamos el buscador
            if (navBuscadorContainer) {
                navBuscadorContainer.classList.remove('d-flex');
                navBuscadorContainer.classList.add('d-none');
            }
            // Mostramos el título central con el nombre de la tienda (solo en pantallas md hacia arriba)
            if (navTituloCentral && textoTituloCentral) {
                navTituloCentral.classList.remove('d-none');
                navTituloCentral.classList.add('d-none', 'd-md-block');
                textoTituloCentral.innerText = tiendaDisp;
            }
        }

        let html = '';

        // El botón "Publicar" SÓLO sale si es Corporación Y la vista NO es 'dashboard'
        if (esCorporacion && tipoVista !== 'dashboard') {
            html += `
            <li class="nav-item">
                <a class="btn btn-warning btn-sm me-3 fw-bold shadow-sm rounded-pill px-3" href="add_product.html">
                    <i class="bi bi-plus-circle-fill me-1"></i>Publicar
                </a>
            </li>`;
        }

        const fotoHtml = usuario.fotoPerfil 
            ? `<img src="http://localhost:8080/uploads/${usuario.fotoPerfil}" class="rounded-circle ms-2" style="width:35px;height:35px;object-fit:cover;">` 
            : `<i class="bi bi-person-circle fs-3 ms-2"></i>`;

        html += `
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-white d-flex align-items-center" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                <div class="text-end me-2 d-none d-sm-block" style="line-height: 1.2;">
                    <div class="fw-bold" style="font-size: 0.9rem;">${nombreDisp}</div>
                    <small class="opacity-75" style="font-size: 0.75rem;">${tipoDisp}</small>
                </div>
                ${fotoHtml}
            </a>
            <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0 mt-2">
                ${esPublicador || esCorporacion ? `
                    <li><h6 class="dropdown-header text-primary"><i class="bi bi-shop me-2"></i>${tiendaDisp}</h6></li>
                    <li><a class="dropdown-item" href="/market.html"><i class="bi bi-shop-window me-2"></i>Market</a></li>
                    <li><a class="dropdown-item" href="dashboard-productos.html"><i class="bi bi-grid-3x3-gap me-2"></i>Mi Inventario</a></li>
                    <li><a class="dropdown-item" href="add_product.html"><i class="bi bi-plus-square me-2"></i>Agregar Producto</a></li>
                    <li><hr class="dropdown-divider"></li>
                ` : ''}
                <li><a class="dropdown-item" href="perfil.html"><i class="bi bi-person-vcard me-2"></i>Mi Perfil</a></li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item text-danger fw-bold" href="#" onclick="window.logout()">
                        <i class="bi bi-box-arrow-right me-2"></i>Cerrar Sesión
                    </a>
                </li>
            </ul>
        </li>`;

        menuUsuario.innerHTML = html;
    } else {
        menuUsuario.innerHTML = `
            <li class="nav-item">
                <a class="nav-link text-white fw-medium" href="login.html">Iniciar Sesión</a>
            </li>
            <li class="nav-item">
                <a class="btn btn-outline-light btn-sm fw-bold px-3 rounded-pill" href="registro.html">Crear Cuenta</a>
            </li>`;
    }
}

// Convertir logout en una función global asignándola a 'window'
window.logout = function() {
    localStorage.removeItem('usuario');
    window.location.href = 'login.html';
};