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


window.onload = function () {
    this.fileInput = document.getElementById('image');
    this.submitButton = document.getElementById('btnSubmit');
    this.fileSelectButton = document.getElementById('imageSelectBtn');
    this.profilePicture = document.getElementById('picture')
    this.imageUrlInput = document.getElementById('imageUrl');
    this.imageSrcBeforeEdit = document.getElementById('picture').src;
};

function checkImageSize() {

    enableSubmitButtonIfDisabled();
    hideSizeError();
    if (fileInput.files.length === 0) {
        profilePicture.src = imageSrcBeforeEdit;
        imageUrlInput.setAttribute("value", imageSrcBeforeEdit);
        return;
    }

    let fileSize = fileInput.files[0].size;
    let fileSizeInMB = fileSize / (1024.0 * 1024.0);
    if (fileSizeInMB > 10) {
        submitButton.disabled = true;
        let sizeErrorElement = document.getElementById('sizeError')
        sizeErrorElement.style.display = 'block'
        sizeErrorElement.textContent = 'The selected file is too large ('
            + fileSizeInMB.toFixed(2) +
            ' MB) . Please select a file no bigger than 10 MB';

    } else {
        const file = fileInput.files[0];
        const reader = new FileReader();
        reader.onload = function (e) {
            profilePicture.src = e.target.result;
        }
        reader.readAsDataURL(file);
        imageUrlInput.setAttribute("value", "newImage");

    }
}

function hideSizeError() {
    document.getElementById('sizeError').style.display = 'none';
}

function clearImageSelection() {

    fileInput.value = null;
    checkImageSize();
}

function deletePicture() {
    profilePicture.src = 'https://res.cloudinary.com/bneikov/image/upload/v1679933174/default_profile_pic_nzgzqa.png'
    fileInput.value = null;
    imageUrlInput.setAttribute("value", "");
    hideSizeError();
    enableSubmitButtonIfDisabled();
}

function enableSubmitButtonIfDisabled() {
    if (submitButton.disabled === true) {
        submitButton.disabled = false;
    }
}

$(document).ready(function () {
    selectArtists();
    selectAlbum();
});
