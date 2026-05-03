const btnTheme = document.querySelector('.btn-theme');
const html = document.documentElement;
const icon = btnTheme.querySelector('div'); 

function initTheme() {
    const savedTheme = localStorage.getItem('theme');

    if (savedTheme) {
        html.setAttribute('data-theme', savedTheme);
    } // nao necessita do else pois o html ja esta com data-theme="dark"
}

function toggleTheme() {
    const currentTheme = html.getAttribute('data-theme');
    
    btnTheme.classList.add('animating');

    setTimeout(() => {
        if (currentTheme === 'dark') {
            html.setAttribute('data-theme', 'light');
            localStorage.setItem('theme', 'light');
        } else {
            html.setAttribute('data-theme', 'dark');
            localStorage.setItem('theme', 'dark');
        }
        
        btnTheme.classList.remove('animating');
    }, 150);
}

initTheme();
btnTheme.addEventListener('click', toggleTheme);