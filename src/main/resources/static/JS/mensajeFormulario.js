
// Obtener la alerta por su ID
let alerta = document.getElementById("alerta");

// Mostrar la alerta
alerta.style.display = "block";
alerta.classList.add("animated", "fadeIn");
setTimeout(function () {
    console.log("Removiendo clase fadeIn...");
    alerta.classList.remove("fadeIn");
    console.log("Agregando clase fadeOut...");
    alerta.classList.add("fadeOut");
    setTimeout(function () {
        console.log("Ocultando alerta...");
        alerta.style.display = "none";
    }, 1000); // tiempo igual a la duración de la animación
}, 3500);