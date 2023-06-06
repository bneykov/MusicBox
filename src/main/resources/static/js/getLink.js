  function getLink(){
    const playlistId = document.getElementById('playlistId').title;
    const requestOptions = {
        method: 'GET',
    };
     fetch('/playlists/' + playlistId + "/create-link", requestOptions)
        .then(response => response.text())
        .then(link => {
            navigator.clipboard.writeText(link)
                .then(() => {
                    const shareButton = document.getElementById('share-button')
                   shareButton.textContent = 'Link to playlist copied'
                    shareButton.classList.add('btn-success')


                })
        })
}