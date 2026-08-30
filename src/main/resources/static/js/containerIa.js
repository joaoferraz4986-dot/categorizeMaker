const pergunta = document.querySelector('.pergunta');
const btnIa = document.querySelector('.btn-ia');
const IaContainer = document.querySelector('.ia');

pergunta.addEventListener('input', () => {
    pergunta.style.height = 'auto';
    pergunta.style.height = pergunta.scrollHeight + 'px';
});

btnIa.addEventListener('click', () => {
    IaContainer.classList.toggle('aberto');
});
