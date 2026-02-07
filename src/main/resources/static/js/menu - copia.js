document.addEventListener("DOMContentLoaded", function() {
    // 1. Obtener sesión
    const sesion = JSON.parse(localStorage.getItem('sesionBadena'));
    const menuContainer = document.getElementById('app-menu');

    // Si no hay contenedor de menú en el HTML, no hacemos nada
    if (!menuContainer) return;

    // Estructura base del Navbar (Bootstrap 5)
    let htmlMenu = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
        <div class="container-fluid">
            <a class="navbar-brand" href="dashboard.html">Badena Marketplace</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
    `;

    // 2. Lógica de Roles: ¿Qué botones mostramos?
    if (sesion) {
        const rol = sesion.tipoUsuario; // ADMIN, CORPORATION, PUBLISHER, MARKET

        // --- ENLACES PARA ADMIN ---
        if (rol === 'ADMIN') {
            htmlMenu += `
                <li class="nav-item"><a class="nav-link" href="admin_users.html">Usuarios</a></li>
                <li class="nav-item"><a class="nav-link" href="admin_tiendas.html">Tiendas</a></li>
                <li class="nav-item"><a class="nav-link" href="admin_reportes.html">Reportes Globales</a></li>
            `;
        }

        // --- ENLACES PARA CORPORATION (Ej: Dueños de la franquicia) ---
        if (rol === 'CORPORATION') {
            htmlMenu += `
                <li class="nav-item"><a class="nav-link" href="corp_resumen.html">Resumen Corp</a></li>
                <li class="nav-item"><a class="nav-link" href="corp_afiliados.html">Afiliados</a></li>
            `;
        }

        // --- ENLACES PARA PUBLISHER (Vendedores/Dueños de Tienda) ---
        if (rol === 'PUBLISHER') {
            htmlMenu += `
                <li class="nav-item"><a class="nav-link" href="dashboard.html">Mi Dashboard</a></li>
                <li class="nav-item"><a class="nav-link" href="add_product.html">Nuevo Producto</a></li>
                <li class="nav-item"><a class="nav-link" href="mis_productos.html">Catálogo</a></li>
                <li class="nav-item"><a class="nav-link" href="ordenes.html">Pedidos</a></li>
            `;
        }

        // --- ENLACES PARA MARKET (Compradores) ---
        if (rol === 'MARKET') {
            htmlMenu += `
                <li class="nav-item"><a class="nav-link" href="marketplace.html">Explorar</a></li>
                <li class="nav-item"><a class="nav-link" href="carrito.html">Mi Carrito</a></li>
                <li class="nav-item"><a class="nav-link" href="mis_compras.html">Mis Compras</a></li>
            `;
        }
    }

    // 3. Cierre del menú y Botón de Usuario/Logout
    htmlMenu += `
                </ul>
                <div class="d-flex align-items-center">
                    ${sesion 
                        ? `<span class="text-white me-3">Hola, <b>${sesion.nombreUsuario}</b> (${sesion.tipoUsuario})</span>
                           <button class="btn btn-outline-danger btn-sm" onclick="cerrarSesion()">Salir</button>`
                        : `<a href="login.html" class="btn btn-primary btn-sm">Iniciar Sesión</a>`
                    }
                </div>
            </div>
        </div>
    </nav>
    `;

    // 4. Inyectar el HTML en la página
    menuContainer.innerHTML = htmlMenu;
});

// Función global de cerrar sesión
function cerrarSesion() {
    Swal.fire({
        title: '¿Cerrar Sesión?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, salir',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('sesionBadena');
            window.location.href = 'login.html';
        }
    });
}