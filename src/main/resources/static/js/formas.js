const bloqueioFormas = document.querySelector('.bloqueio-formas');

Array.from({ length: 25 }).forEach(() => {
    const forma = document.createElement('div');
    const tamanho = Math.random() * 80 + 40;
    const ehHex = Math.random() > 0.5;

    forma.classList.add('forma');
    forma.style.width = `${tamanho}px`;
    forma.style.height = `${tamanho}px`;
    forma.style.left = `${Math.random() * 90}%`;
    forma.style.top = `${Math.random() * 100}%`;
    forma.style.animationDuration = `${Math.random() * 10 + 5}s`;
    forma.style.animationDelay = `${Math.random() * 5}s`;
    forma.style.border = "3px solid white";
    forma.style.borderRadius = ehHex ? "0%" : "50%";

    bloqueioFormas.appendChild(forma);
});