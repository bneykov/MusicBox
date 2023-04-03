let table = document.createElement('table');
table.classList.add('table', 'table-striped', 'table-hover');
table.id = 'table';

function loadUsers() {
    table.innerHTML = ""

    let requestOptions = {
        method: 'GET',
        redirect: 'follow',
    };
    fetch('/users/get', requestOptions)
        .then(response => response.json())
        .then(users => {
            let table = generateTable(users);
            document.getElementById('tableParent').appendChild(table)
        })
        .catch(error => console.log('Users fetch error', error));


}


function generateTable(users) {


    let headerRow = table.insertRow();
    let headers = ['Username', 'Name', 'Email', 'Last Modified', 'Last Logged In', 'Role'];

    headers.forEach(function (header) {
        let th = document.createElement('th');
        th.innerText = header;
        th.classList.add('text-light', 'text-center')
        headerRow.appendChild(th);
    });

    users.forEach(async function (user) {


        let row = table.insertRow();
        row.classList.add('text-light', 'text-center');


        let usernameTd = document.createElement('td');
        usernameTd.innerText = user.username;
        row.appendChild(usernameTd);

        let nameTd = document.createElement('td');
        nameTd.innerText = user.name;
        row.appendChild(nameTd);

        let emailTd = document.createElement('td');
        emailTd.innerText = user.email;
        row.appendChild(emailTd);

        let modifiedTd = document.createElement('td');
        modifiedTd.innerText = user.modified;
        row.appendChild(modifiedTd);

        let lastLoggedInTd = document.createElement('td');
        lastLoggedInTd.innerText = user.lastLoggedIn;
        row.appendChild(lastLoggedInTd);

        let selectTd = document.createElement('td');
        let select = document.createElement('select');
        select.onchange = function () {
            changeRole(user.id);
        };
        select.classList.add('browser-default', 'form-select');
        select.id = user.username + '_role';
        select.name = user.username + '_role';
        select.title = "select";

        let userOption = document.createElement('option');
        userOption.value = 'USER';
        userOption.innerText = 'User';
        if (user.roles.length === 1) {
            userOption.selected = true;
        }
        select.appendChild(userOption);

        let adminOption = document.createElement('option');
        adminOption.value = 'ADMIN';
        adminOption.innerText = 'Admin';
        if (user.roles.length > 1) {
            adminOption.selected = true;
        }
        select.appendChild(adminOption);
        selectTd.appendChild(select);
        row.appendChild(selectTd);
    });

    return table;
}

async function changeRole(userId) {
    const requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    await fetch('/users/' + userId + '/change_role', requestOptions).then()
        .catch(error => console.log('Role change error', error));
    loadUsers();
}

$(document).ready(function () {
    loadUsers();
});






