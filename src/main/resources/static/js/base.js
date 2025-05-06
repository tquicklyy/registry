function updateInputDateClass(inputDate) {
    if (inputDate.value) {
        inputDate.classList.add('input-date-without-opacity');
        inputDate.classList.remove('input-date-with-opacity');
    } else {
        inputDate.classList.add('input-date-with-opacity');
        inputDate.classList.remove('input-date-without-opacity');
    }
}

document.addEventListener("DOMContentLoaded", function() {
    var inputElements = document.querySelectorAll(".date-input");

    inputElements.forEach(function(input) {
        updateInputDateClass(input);
    });
});