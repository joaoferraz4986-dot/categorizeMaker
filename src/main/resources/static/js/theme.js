const btnTheme = document.querySelector('.btn-theme');
const html = document.documentElement;
const icon = document.querySelector('.icon');


function initTheme() {
    const savedTheme = localStorage.getItem('theme');
    
    if (savedTheme) {
        html.setAttribute('data-theme', savedTheme);
    } else {
        html.setAttribute('data-theme', 'light');
    }
}
function toggleTheme(){
    const currentTheme = html.getAttribute('data-theme');
    if (currentTheme === 'dark'){
        html.setAttribute('data-theme', 'light');
        localStorage.setItem('theme','light');
        icon.style.backgroundImage = "url(imgs/sun.svg)";
    } else {
        html.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme','dark');
        icon.style.backgroundImage = "url(imgs/moon.svg)";
    }
}
initTheme();

btnTheme.addEventListener('click', toggleTheme);