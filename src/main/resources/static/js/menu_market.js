// Archivo: /js/menu_market.js

document.addEventListener("DOMContentLoaded", function() {
    renderizarMenuUsuario();
});

function renderizarMenuUsuario() {
    const sesion = JSON.parse(localStorage.getItem('sesionBadena'));
    const menuContainer = document.getElementById('menuUsuario');
    
    // Si no encuentra el contenedor en el HTML, se detiene
    if (!menuContainer) return;

    let htmlBotones = '';

    if (!sesion) {
        // --- OPCIÓN A: USUARIO INVITADO ---
        htmlBotones = `
            <li class="nav-item">
                <span class="navbar-text text-white-50 small me-3">¿Eres proveedor?</span>
                <a href="login.html" class="btn btn-outline-light btn-sm px-3 fw-bold">Acceso Socios</a>
            </li>
        `;
    } else {
        // --- OPCIÓN B: USUARIO LOGUEADO ---
        htmlBotones = `
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle text-white d-flex align-items-center bg-transparent border-0" href="#" role="button" data-bs-toggle="dropdown">
                    <div class="d-flex flex-column text-end me-2" style="line-height: 14px;">
                        <span class="fw-bold small">${sesion.nombreUsuario}</span>
                        <small style="font-size: 10px;" class="text-white-50">${sesion.tipoUsuario}</small>
                    </div>
                    <i class="bi bi-person-circle fs-4"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-end shadow">
                    <li><h6 class="dropdown-header">Gestión</h6></li>
                    <li><a class="dropdown-item" href="perfil.html"><i class="bi bi-person-badge me-2"></i>Mi Perfil</a></li>
        `;

        // Botón especial si es Vendedor o Admin
        if (sesion.tipoUsuario === 'PUBLISHER' || sesion.tipoUsuario === 'ADMIN') {
            htmlBotones += `
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-primary fw-bold bg-light" href="dashboard.html">
                    <i class="bi bi-speedometer2 me-2"></i>Panel de Administración
                </a></li>
            `;
        }


        // Botón especial si es CORPORATION
        if (sesion.tipoUsuario === 'CORPORATION' || sesion.tipoUsuario === 'ADMIN') {
            htmlBotones += `
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-primary fw-bold bg-light" href="dashboard.html">
                    <i class="bi bi-speedometer2 me-2"></i>Panel de Administración
                    </a>
                </li>
                <li><a class="dropdown-item text-primary fw-bold bg-light" href="add_product.html">
                    <i class="bi bi-bag-plus-fill me-2"></i>Agregar producto
                </a></li>

                <li><a class="dropdown-item text-primary fw-bold bg-light" href="puplisher.html">
                    <i class="bi bi-people me-2"></i>Publicadores
                </a></li>


            `;
        }




        // Botón Salir
        htmlBotones += `
                    <li><hr class="dropdown-divider"></li>
                    <li><button class="dropdown-item text-danger" onclick="cerrarSesionMarket()">
                        <i class="bi bi-power me-2"></i>Cerrar Sesión
                    </button></li>
                </ul>
            </li>
        `;
    }

    menuContainer.innerHTML = htmlBotones;
}

function cerrarSesionMarket() {
    Swal.fire({
        title: '¿Salir?',
        text: 'Cerrarás tu sesión actual.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, salir',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('sesionBadena');
            window.location.reload();
        }
    });
}