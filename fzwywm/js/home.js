(function () {
    var trigger = document.getElementById('fuSeal');
    var dialog = document.getElementById('fortuneDialog');
    if (!trigger || !dialog || typeof dialog.showModal !== 'function') return;

    var closeBtn = document.getElementById('fortuneClose');
    var dismissBtn = document.getElementById('fortuneDismiss');
    var textEl = document.getElementById('fortuneText');
    var dateEl = document.getElementById('fortuneDate');

    var fortunes = [
        '福气满满，学业顺遂，万事皆可期。',
        '平安喜乐，前程似锦，岁岁常欢愉。',
        '春风得意，少年有志，步步皆荣光。',
        '福满身心，万事和顺，所行皆坦途。',
        '时来运转，喜乐常伴，多喜乐长安宁。',
        '闽都纳福，校园逐梦，理想皆可达。',
        '福如东海，寿比南山，吉祥常伴左右。',
        '校园勤学，榕城纳福，梦想终会开花。'
    ];

    function dailyIndex() {
        var now = new Date();
        var seed = now.getFullYear() * 372 + (now.getMonth() + 1) * 31 + now.getDate();
        return seed % fortunes.length;
    }

    function openDialog() {
        var now = new Date();
        textEl.textContent = fortunes[dailyIndex()];
        if (dateEl) {
            dateEl.textContent = now.getFullYear() + '.' +
                String(now.getMonth() + 1).padStart(2, '0') + '.' +
                String(now.getDate()).padStart(2, '0');
        }
        dialog.showModal();
        document.body.classList.add('dialog-open');
    }

    function closeDialog() {
        if (dialog.open) dialog.close();
    }

    trigger.addEventListener('click', function (event) {
        event.preventDefault();
        openDialog();
    });
    if (closeBtn) closeBtn.addEventListener('click', closeDialog);
    if (dismissBtn) dismissBtn.addEventListener('click', closeDialog);

    dialog.addEventListener('close', function () {
        document.body.classList.remove('dialog-open');
        trigger.focus();
    });

    dialog.addEventListener('click', function (event) {
        if (event.target === dialog) closeDialog();
    });
})();

(function () {
    var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    var stats = document.querySelector('.stats');
    if (!stats || reduceMotion) return;

    var numbers = [].filter.call(stats.querySelectorAll('.stat b'), function (el) {
        return /^\d+$/.test(el.textContent.trim());
    });
    if (!numbers.length) return;

    var finals = numbers.map(function (el) { return el.textContent.trim(); });
    var started = false;

    function finish() {
        numbers.forEach(function (el, i) {
            el.textContent = finals[i];
        });
    }

    function countUp(el, finalText) {
        var target = parseInt(finalText, 10);
        var digits = finalText.length;
        var start = null;

        function step(now) {
            if (start === null) start = now;
            var p = Math.min(1, (now - start) / 400);
            var eased = 1 - Math.pow(1 - p, 3);
            el.textContent = String(Math.round(target * eased)).padStart(digits, '0');
            if (p < 1) window.requestAnimationFrame(step);
        }

        window.requestAnimationFrame(step);
        window.setTimeout(function () {
            el.textContent = finalText;
        }, 600);
    }

    function start() {
        if (started) return;
        started = true;
        numbers.forEach(function (el, i) {
            countUp(el, finals[i]);
        });
    }

    if (!('IntersectionObserver' in window)) {
        start();
        return;
    }

    var observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (!entry.isIntersecting) return;
            start();
            observer.disconnect();
        });
    }, { threshold: 0.4 });

    observer.observe(stats);
    window.setTimeout(function () {
        if (!started) {
            start();
            observer.disconnect();
        }
    }, 2600);
    window.setTimeout(finish, 3400);
})();
