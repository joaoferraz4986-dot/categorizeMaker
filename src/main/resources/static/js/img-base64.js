function handlePhotoUpload(e) {
    const file = e.target.files[0];
    if (!file) return;

    const max = 5 * 1024 * 1024;
    if (file.size > max) {
        showToast('Imagem maior que 5MB', 'error');
        return;
    }

    const reader = new FileReader();

    reader.onload = (ev) => {
        state.pendingPhoto = ev.target.result;
        $('#photo-preview').src = state.pendingPhoto;
        $('#photo-preview').style.display = 'block';
        $('#photo-hint').style.display = 'none';
    };

    reader.readAsDataURL(file);
}