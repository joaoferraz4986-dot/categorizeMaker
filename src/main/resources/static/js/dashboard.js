document.querySelectorAll('.navLink').forEach(function (link) {
    link.addEventListener('click', function () {
        document.querySelectorAll('.navLink').forEach(function (item) {
            item.classList.remove('active');
        });
        link.classList.add('active');
 
        const alvo = link.dataset.cat;
        const pagina = document.querySelector('.pagina[data-pagina="' + alvo + '"]');
        if (pagina) {
            document.querySelectorAll('.pagina').forEach(function (p) {
                p.hidden = true;
            });
            pagina.hidden = false;
        }
    });
});