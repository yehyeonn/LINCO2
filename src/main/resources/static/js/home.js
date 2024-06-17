document.addEventListener("DOMContentLoaded", () => {
    const sections = document.querySelectorAll('.category');

    // Intersection Observer Options
    const options = {
        root: null,
        rootMargin: '0px',
        threshold: 0.7 // 뷰포트의 70% 이상 보일 때 작동
    };

    // Intersection Observer Callback
    const callback = (entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            } else {
                entry.target.classList.remove('visible');
            }
        });
    };

    // Create Intersection Observer
    const observer = new IntersectionObserver(callback, options);

    // Observe each section
    sections.forEach(section => {
        observer.observe(section);
    });

    // 초기 로드 시 첫 번째 섹션을 가시화
    if (sections.length > 0) {
        sections[0].classList.add('visible');
    }
});
