(function () {
    var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    var selectors = [
        '.f-bar .bar-head',
        '.f-hero-inner > *',
        '.entry',
        '.stem-aside',
        '.stem-article',
        '.site-note',
        '.closing-panel',
        '.f-hero-inner',
        '.sec-head',
        '.stage-wrap',
        '.map-panel',
        '.map-note',
        '.tile',
        '.gallery-item',
        '.waterfall-card',
        '.timeline-item',
        '.fu-sign-box',
        '.mast-inner',
        '.feature-media',
        '.feature-text',
        '.feat',
        '.index-head',
        '.index-list li',
        '.sources-inner',
        '.side',
        '.module',
        '.colophon-head',
        '.colophon-rail',
        '.colophon-section',
        '.zone-head',
        '.tech-card',
        '.wide-card',
        '.rail'
    ];

    var seen = new Set();
    var targets = [];

    selectors.forEach(function (selector) {
        document.querySelectorAll(selector).forEach(function (el) {
            if (seen.has(el)) return;
            seen.add(el);
            targets.push(el);
        });
    });

    if (!targets.length) return;

    if (reduceMotion || !('IntersectionObserver' in window)) {
        targets.forEach(function (el) {
            el.classList.add('reveal', 'is-visible');
        });
        return;
    }

    var groups = new Map();

    targets.forEach(function (el) {
        var parent = el.parentElement;
        var index = groups.get(parent) || 0;
        groups.set(parent, index + 1);
        el.style.transitionDelay = Math.min(index, 4) * 60 + 'ms';
        el.classList.add('reveal');
    });

    var observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (!entry.isIntersecting) return;
            entry.target.classList.add('is-visible');
            observer.unobserve(entry.target);
        });
    }, {
        threshold: 0.01,
        rootMargin: '120px 0px 120px 0px'
    });

    targets.forEach(function (el) {
        observer.observe(el);
    });

    window.setTimeout(function () {
        targets.forEach(function (el) {
            if (!el.classList.contains('is-visible')) {
                el.classList.add('is-visible');
                observer.unobserve(el);
            }
        });
    }, 2200);
})();
