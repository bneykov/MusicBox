$(document).ready(function () {
    $('#artistSearch').keyup(function () {
        let inputVal = $(this).val().toLowerCase();
        let items = document.getElementsByClassName("form-check");

        for (let i = 0; i < items.length; i++) {

            let label = items[i].getElementsByTagName("label")[0];

            if (label.innerHTML.toLowerCase().indexOf(inputVal) > -1) {
                items[i].style.display = "";
            } else {
                items[i].style.display = "none";
            }
        }


    });


    $('#artistsList').on('click', function (event) {
        event.stopPropagation();
    });
    $(".artist-checkbox").change( function () {

        let selectedArtists = $("input.artist-checkbox:checked").map(function () {
            return $(this).siblings("label").text()
        }).get();
        selectedArtists = selectedArtists.join(", ")

        if (selectedArtists.length > 38) {
            selectedArtists = selectedArtists.substring(0, 38) + "...";
            $("#artistDropdown").text(selectedArtists);
        } else if (selectedArtists.length > 0) {
            $("#artistDropdown").text(selectedArtists);
        } else{
            $("#artistDropdown").text("Select Artists");
        }

    });

});