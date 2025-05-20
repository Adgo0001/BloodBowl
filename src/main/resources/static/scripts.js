window.addEventListener('load', () => {
    if (window.location.hash === '#table-anchor') {
        const element = document.getElementById('table-anchor');
        if (element) {
            element.scrollIntoView({ behavior: 'smooth' });
        }
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const createTournamentBtn = document.getElementById("create-tournament-btn");
    const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true";
    const hasAcceptedRules = sessionStorage.getItem("hasAcceptedRules") === "true";

    if (createTournamentBtn) {
        if (!isLoggedIn || !hasAcceptedRules) {
            createTournamentBtn.disabled = true;
            createTournamentBtn.textContent = "Du skal være logget ind og acceptere reglerne for at oprette en turnering.";
        }

        createTournamentBtn.addEventListener("click", function() {
            if (isLoggedIn && hasAcceptedRules) {
                window.location.href = "/create_tournament.html";
            } else {
                alert("Du skal være logget ind og acceptere reglerne før du kan oprette en turnering.");
                window.location.href = "/login_or_accept_rules.html";
            }
        });
    }
});

// Debounce-funktion for at undgå for mange kald under hurtig skrivning
function debounce(func, delay) {
    let timeoutId;
    return function(...args) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func.apply(this, args), delay);
    };
}

function filterUsers() {
    const input = document.getElementById("searchInput");
    const filter = input.value.toLowerCase();
    const table = document.getElementById("userTable");
    const rows = table.getElementsByTagName("tr");
    const noResultsMessage = document.getElementById("noResultsMessage");
    let visibleCount = 0;

    for (let i = 1; i < rows.length; i++) { // Spring header over
        const row = rows[i];
        const cells = row.getElementsByTagName("td");
        let rowText = "";

        for (let j = 0; j < cells.length; j++) {
            rowText += cells[j].textContent.toLowerCase() + " ";
        }

        if (rowText.includes(filter)) {
            row.style.display = "";
            visibleCount++;
        } else {
            row.style.display = "none";
        }
    }

    if (visibleCount === 0) {
        table.style.display = "none";               // Skjul tabellen
        noResultsMessage.style.display = "block";  // Vis besked
    } else {
        table.style.display = "";                    // Vis tabellen
        noResultsMessage.style.display = "none";    // Skjul besked
    }
}


// Sæt debounce på input-eventet
document.getElementById("searchInput").addEventListener('input', debounce(filterUsers, 250));



let sortDirection = true; // true = ascending, false = descending

function sortTable(columnIndex) {
    const table = document.getElementById("userTable");
    let switching = true;
    let shouldSwitch;
    let rows, i;

    while (switching) {
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            const x = rows[i].getElementsByTagName("TD")[columnIndex];
            const y = rows[i + 1].getElementsByTagName("TD")[columnIndex];

            if (sortDirection) {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
    sortDirection = !sortDirection; // Skift retning næste gang
}
