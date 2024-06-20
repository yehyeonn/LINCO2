const cardContainer = document.querySelector('.card-container');
const cards = document.querySelectorAll('.card-article');
let currentIndex = 0;
const totalCards = cards.length;
const cardsPerSlide = 5;

function showSlide(index) {
    cardContainer.style.transform = `translateX(-${index * 100}%)`;
}

document.querySelector('.slide-next').addEventListener('click', () => {
    if (currentIndex < Math.ceil(totalCards / cardsPerSlide) - 1) {
        currentIndex++;
    } else {
        currentIndex = 0;
    }
    showSlide(currentIndex);
});

document.querySelector('.slide-prev').addEventListener('click', () => {
    if (currentIndex > 0) {
        currentIndex--;
    } else {
        currentIndex = Math.ceil(totalCards / cardsPerSlide) - 1;
    }
    showSlide(currentIndex);
});