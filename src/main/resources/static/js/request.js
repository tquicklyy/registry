function sendForm(idOfForm, queryString, requestId) {
    var form = document.getElementById(idOfForm);
    const action = form.action.split('?')[0];
    var dateSelect = document.getElementById('date-select');
    if(queryString === 'next-status=IN_WORK') {
        form.action = action  + '?' + queryString + '&requestId=' + requestId + '&date=' + dateSelect.value;
    } else {
        form.action = action  + '?' + queryString + '&requestId=' + requestId;
    }
    window.location.href= form.action;
    return false;
}
