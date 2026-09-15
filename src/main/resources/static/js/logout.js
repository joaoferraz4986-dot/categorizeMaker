import { logout } from './services/authGuard.js';

const button = document.querySelector('#logout-button');
if (button) {
    button.addEventListener('click', logout);
}
