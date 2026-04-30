import { authService } from './authService.js';

// cad
const formCadastro = document.querySelector('.cad-inner');

formCadastro.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const dadosUsuario = {
        nome:  document.querySelector('.cad-inner #nome').value,
        email: document.querySelector('.cad-inner #email').value,
        senha: document.querySelector('.cad-inner #senha').value
    };

    try {
        await authService.cadastrar(dadosUsuario);
        alert('Conta criada com sucesso! Faça login para continuar.');
        document.getElementById('troca-1').click();
    } catch (erro) {
        console.error('Erro ao cadastrar:', erro);
        alert('Não foi possível criar a conta. Tente novamente.');
    }
});

// log

const formLogin = document.querySelector('.log-inner');

formLogin.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const credenciais = {
        email: document.querySelector('.log-inner #email').value,
        senha: document.querySelector('.log-inner #senha').value
    };

    try {
        const resposta = await authService.login(credenciais);
        localStorage.setItem('token', resposta.token);
        window.location.href = '/lab.html';
    } catch (erro) {
        console.error('Erro ao fazer login:', erro);
        alert('Email ou senha incorretos. Tente novamente.');
    }
});