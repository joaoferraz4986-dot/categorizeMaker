const bloqueio = document.querySelector('.bloqueio');
const loginBtn = document.querySelector('#troca-1');
const registroBtn = document.querySelector('#troca-2');

loginBtn.addEventListener('click', () => {
    bloqueio.classList.add('active');
});

registroBtn.addEventListener('click', () => {
    bloqueio.classList.remove('active');
});