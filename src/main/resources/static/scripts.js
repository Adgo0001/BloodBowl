document.addEventListener("DOMContentLoaded", function() {
    const createTournamentBtn = document.getElementById("create-tournament-btn");

    // Tjek om brugeren er logget ind og har accepteret reglerne
    const isLoggedIn = sessionStorage.getItem("isLoggedIn") === "true"; // Tjekker om brugeren er logget ind
    const hasAcceptedRules = sessionStorage.getItem("hasAcceptedRules") === "true"; // Tjekker om reglerne er accepteret

    // Hvis brugeren ikke er logget ind eller ikke har accepteret reglerne
    if (!isLoggedIn || !hasAcceptedRules) {
        createTournamentBtn.disabled = true; // Deaktiver knappen, hvis betingelserne ikke er opfyldt
        createTournamentBtn.textContent = "Du skal være logget ind og acceptere reglerne for at oprette en turnering."; // Ændrer tekst på knappen
    }

    // Hvis brugeren er logget ind og har accepteret reglerne, gør knappen aktiv og før brugeren videre
    createTournamentBtn.addEventListener("click", function() {
        if (isLoggedIn && hasAcceptedRules) {
            window.location.href = "/create_tournament.html"; // Skifter til siden hvor brugeren kan oprette en turnering
        } else {
            alert("Du skal være logget ind og acceptere reglerne før du kan oprette en turnering.");
            window.location.href = "/login_or_accept_rules.html"; // Send brugeren til login/accept af regler
        }
    });
});
