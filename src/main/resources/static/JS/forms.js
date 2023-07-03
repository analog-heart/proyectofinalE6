// Esto es para que cuando el usuario cambie de pestaña sin completar el formulario, se le avise cambiando título de página
let previousTitle = document.title;
console.log(previousTitle);
window.addEventListener("blur", () => {
  previousTitle = document.title;
  document.title = "Soluciones Activas: Complatar Formulario!";
});

window.addEventListener("focus", () => {
  document.title = previousTitle;
});
// fin funcion cambio de titulo