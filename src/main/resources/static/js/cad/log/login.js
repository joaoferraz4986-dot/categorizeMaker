import { authService } from '../../services/authService.js';

const formLogin = document.querySelector('.log-inner');

formLogin.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const credenciais = {
        email: document.getElementById('login-email').value.trim(),
        senha: document.getElementById('login-senha').value
    };

    try {
        const resposta = await authService.login(credenciais);
        localStorage.setItem('token', resposta.token);
        window.location.href = './lab.html';
    } catch (erro) {
        console.error('Erro ao fazer login:', erro);
        alert('Email ou senha incorretos. Tente novamente.');
    }
});