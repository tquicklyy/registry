function updateStatusClass(pWithStatus) {
    if (pWithStatus.textContent === 'Ожидание') {
        pWithStatus.classList.add('status-text-wait');
        pWithStatus.classList.remove('status-text-in-work');
        pWithStatus.classList.remove('status-text-accept');
        pWithStatus.classList.remove('status-text-cancel');
    } else if(pWithStatus.textContent === 'В работе') {
        pWithStatus.classList.remove('status-text-wait');
        pWithStatus.classList.add('status-text-in-work');
        pWithStatus.classList.remove('status-text-accept');
        pWithStatus.classList.remove('status-text-cancel');
    } else if(pWithStatus.textContent === 'Подтверждена') {
        pWithStatus.classList.remove('status-text-wait');
        pWithStatus.classList.remove('status-text-in-work');
        pWithStatus.classList.add('status-text-accept');
        pWithStatus.classList.remove('status-text-cancel');
    } else {
        pWithStatus.classList.remove('status-text-wait');
        pWithStatus.classList.remove('status-text-in-work');
        pWithStatus.classList.remove('status-text-accept');
        pWithStatus.classList.add('status-text-cancel');
   }
}

document.addEventListener("DOMContentLoaded", function() {
    var pWithStatus = document.querySelectorAll(".status-text");

    inputElements.forEach() {
        function(input) {
            updateInputDateClass(pWithStatus);
        }
    }
}