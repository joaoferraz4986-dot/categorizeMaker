import { authService } from '../../services/authService.js';

const formCadastro = document.querySelector('.cad-inner');

formCadastro.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const nome  = document.getElementById('cadastro-nome').value.trim();
    const email = document.getElementById('cadastro-email').value.trim();
    const senha = document.getElementById('cadastro-senha').value;
    
    const dadosUsuario = { nome, email, senha };

    try {
        await authService.cadastrar(dadosUsuario);
        alert('Conta criada com sucesso! Faça login para continuar.');
        document.getElementById('troca-log').click();
    } catch (erro) {
        console.error('Erro ao cadastrar:', erro);
        alert('Não foi possível criar a conta. Tente novamente.');
    }
});