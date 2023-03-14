

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
$(document).ready(function() {
    selectArtists();
    selectAlbum();
});
