function filterListByInput(inputId, listId) {
    const inputField = document.getElementById(inputId);
    const filter = inputField.value.toLowerCase();
    const lists = document.querySelectorAll('#' + listId);

    lists.forEach((li) => {
        const label = li.querySelector('label')
        const labelText = label.textContent.toLowerCase();
        if (labelText.includes(filter)) {
            li.style.display = '';
        } else {
            li.style.display = 'none';
        }
    });
}

function selectArtists() {
    let selectedArtists = $("input.artist-checkbox:checked").map(function () {
        return $(this).siblings("label").text()
    }).get();
    selectedArtists = selectedArtists.join(", ")

    if (selectedArtists.length > 38) {
        selectedArtists = selectedArtists.substring(0, 38) + "...";
        $("#artistDropdown").text(selectedArtists);
    } else if (selectedArtists.length > 0) {
        $("#artistDropdown").text(selectedArtists);
    } else {
        $("#artistDropdown").text("Select Artists");
    }
}

function selectAlbum() {
    let selectedAlbum = $("input.album-radio:checked").map(function () {
        return $(this).siblings("label").text()
    }).get();

    if (selectedAlbum.length > 0) {
        $("#albumDropdown").text(selectedAlbum);
    } else {
        $("#albumDropdown").text("Select Album");
    }
}


function checkImageSize() {
    let fileInput = document.getElementById('image');
    let submitButton = document.getElementById('btnSubmit');

    if (submitButton.disabled === true){
        fileInput.classList.remove('is-invalid', 'alert-danger')
        fileInput.nextSibling.remove();
        submitButton.disabled = false;
    }
    if (fileInput.files.length === 0){
        return;
    }

    let fileSize = fileInput.files[0].size;
    let fileSizeInMB = fileSize / (1024.0 * 1024.0);
    if (fileSizeInMB > 10) {
        submitButton.disabled = true;
        let errorMessage = document.createElement('small');
        errorMessage.classList.add('text-warning')
        fileInput.classList.add('is-invalid', 'alert-danger')
        errorMessage.innerHTML = 'The selected file is too large ('
            + fileSizeInMB.toFixed(2) +
            ' MB) . Please select a file no bigger than 10 MB';
        let errorMessageContainer = document.createElement('p')
        errorMessageContainer.appendChild(errorMessage)
        fileInput.parentNode.insertBefore(errorMessageContainer, fileInput.nextSibling);

    }
}

$(document).ready(function () {
    selectArtists();
    selectAlbum();
});
