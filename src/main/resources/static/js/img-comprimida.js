export function handlePhotoUpload(e, state, showToast, $) {
    const file = e.target.files[0];
    if (!file) return;

    const max = 5 * 1024 * 1024;
    if (file.size > max) {
        showToast('Imagem maior que 5MB', 'error');
        return;
    }

    const reader = new FileReader();
    reader.onload = (ev) => {
        // Cria uma imagem em memória para conseguirmos ler as dimensões
        const img = new Image();
        img.src = ev.target.result;

        img.onload = () => {
            // Define o tamanho máximo aceitável (ex: 800px)
            const MAX_WIDTH = 800;
            const MAX_HEIGHT = 800;
            let width = img.width;
            let height = img.height;

            // Redimensiona mantendo a proporção da imagem
            if (width > height) {
                if (width > MAX_WIDTH) {
                    height = Math.round((height * MAX_WIDTH) / width);
                    width = MAX_WIDTH;
                }
            } else {
                if (height > MAX_HEIGHT) {
                    width = Math.round((width * MAX_HEIGHT) / height);
                    height = MAX_HEIGHT;
                }
            }

            // Cria um canvas para desenhar a imagem menor
            const canvas = document.createElement('canvas');
            canvas.width = width;
            canvas.height = height;

            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, width, height);

            // Exporta a imagem comprimida em formato WebP (70% de qualidade)
            const compressedBase64 = canvas.toDataURL('image/webp', 0.7);

            // Salva a nova versão comprimida no seu state
            state.pendingPhoto = compressedBase64;
            $('#photo-preview').src = state.pendingPhoto;
            $('#photo-preview').style.display = 'block';
            $('#photo-hint').style.display = 'none';
        };
    };

    // Inicia a leitura do arquivo
    reader.readAsDataURL(file);
}