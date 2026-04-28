const tampa = document.querySelector('.tampa');
const loginBtn = document.querySelector('#troca-1');
const registroBtn = document.querySelector('#troca-2');

loginBtn.addEventListener('click', () => {
    tampa.classList.add('active');
});

registroBtn.addEventListener('click', () => {
    tampa.classList.remove('active');
});