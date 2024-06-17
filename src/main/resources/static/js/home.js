document.addEventListener('DOMContentLoaded', () => {
    const sections = document.querySelectorAll('.category');
    let currentSectionIndex = 0;
    let isScrolling = false;

    const showSection = (index) => {
        sections.forEach((section, i) => {
            if (i === index) {
                section.classList.add('visible');
                section.style.display = 'flex';
            } else {
                section.classList.remove('visible');
                section.style.display = 'none';
            }
        });
    };

    const scrollHandler = (event) => {
        if (isScrolling) return;

        if (event.deltaY > 0 && currentSectionIndex < sections.length - 1) {
            currentSectionIndex++;
        } else if (event.deltaY < 0 && currentSectionIndex > 0) {
            currentSectionIndex--;
        }

        showSection(currentSectionIndex);
        isScrolling = true;

        setTimeout(() => {
            isScrolling = false;
        }, 500); // 스크롤 이벤트 사이의 딜레이를 1초로 설정
    };

    window.addEventListener('wheel', scrollHandler);
    showSection(currentSectionIndex);  // 처음에 첫 번째 섹션을 표시
});