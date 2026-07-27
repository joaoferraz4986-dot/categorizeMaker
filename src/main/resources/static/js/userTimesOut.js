import {logout} from './authGuard.js';

function decodificarToken(token){
    try{
        const decodificadoBase64 = token.split('.')[1];
        const decodificadoJson = atob(decodificadoBase64);
        return JSON.parse(decodificadoJson);
    } catch (erro) {
        return null;
    }
}

function calcularTempoRestante(token){
    const decodificado = decodificarToken(token);

    if (!decodificado || !decodificado.exp) return 0;

    // fiz isso porq quando vem o tempo do Date.now, ele vem em milisegundos e o exp do token em segundos
    const agoraEmSegundos = Math.floor(Date.now() / 1000);
    const tempoRestanteEmSegundos = decodificado.exp - agoraEmSegundos;

    return tempoRestanteEmSegundos * 1000;
}

function iniciarVisaoDeSessão(){
    const token = localStorage.getItem('token');

    if (!token){
        logout();
        return;
    }

    const tempoRestante = calcularTempoRestante(token);

    if (tempoRestante <= 0){
        logout();
        return;
    }

    setTimeout(logout, tempoRestante);
}

iniciarVisaoDeSessão();
