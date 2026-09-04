document.querySelectorAll('.navLink').forEach(function (link) {
    link.addEventListener('click', function () {
        document.querySelectorAll('.navLink').forEach(function (item) {
            item.classList.remove('active');
        });
        link.classList.add('active');
    });
});