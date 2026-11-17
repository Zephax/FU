(function () {
    'use strict';

    var api = window.FZApi;
    var MAX_FORTUNE = 100;

    var meterNum = document.getElementById('meterNum');
    var meterFill = document.getElementById('meterFill');
    var toastEl = document.getElementById('toast');
    var toastTimer = null;

    var tube = document.getElementById('tube');
    var drawBtn = document.getElementById('drawBtn');
    var lotteryResult = document.getElementById('lotteryResult');
    var drawing = false;
    var dailyText = '';

    var lanterns = Array.prototype.slice.call(document.querySelectorAll('.lantern'));
    var lampCount = document.getElementById('lampCount');
    var litCount = 0;

    var cards = Array.prototype.slice.call(document.querySelectorAll('.flip'));

    var blessListDom = document.getElementById('blessList');
    var blessForm = document.getElementById('blessForm');
    var submitBtn = blessForm ? blessForm.querySelector('button[type="submit"]') : null;

    var luckList = [
        '福气满满，学业顺遂，万事皆可期。',
        '平安喜乐，前程似锦，岁岁常欢愉。',
        '春风得意，少年有志，步步皆荣光。',
        '福满身心，万事和顺，所行皆坦途。',
        '时来运转，喜乐常伴，多喜乐长安宁。',
        '闽都纳福，校园逐梦，理想皆可达。',
        '福如东海，寿比南山，吉祥常伴左右。',
        '校园勤学，榕城纳福，梦想终会开花。'
    ];

    function toast(msg) {
        toastEl.textContent = msg;
        toastEl.classList.add('show');
        if (toastTimer) clearTimeout(toastTimer);
        toastTimer = setTimeout(function () {
            toastEl.classList.remove('show');
        }, 2200);
    }

    function fail(error) {
        toast(error && error.message ? error.message : '网络开小差了，请稍后再试');
    }

    function setFortune(value, max) {
        var ceiling = max || MAX_FORTUNE;
        var safe = Math.max(0, Math.min(ceiling, Number(value) || 0));
        meterNum.textContent = safe;
        meterFill.style.width = (safe / ceiling * 100) + '%';
    }

    function lockDraw() {
        drawBtn.disabled = true;
        drawBtn.textContent = '今日福签已抽取';
    }

    function restoreState() {
        api.fortuneState().then(function (state) {
            dailyText = state.daily && state.daily.text ? state.daily.text : '';
            setFortune(state.fortune, state.max);

            var lamps = state.lamps || [];
            litCount = 0;
            lanterns.forEach(function (lamp, index) {
                if (lamps.indexOf(String(index + 1)) >= 0) {
                    lamp.classList.add('on');
                    lamp.setAttribute('aria-pressed', 'true');
                    litCount++;
                }
            });
            lampCount.textContent = litCount;

            var flipped = state.cards || [];
            cards.forEach(function (card, index) {
                if (flipped.indexOf(String(index + 1)) >= 0) {
                    card.classList.add('flipped');
                }
            });

            if (state.drawn) {
                lotteryResult.textContent = dailyText || '今日福签已抽取，明天再来看看。';
                lockDraw();
            }
        }).catch(fail);
    }

    function drawFortune() {
        if (drawing) return;
        drawing = true;
        drawBtn.disabled = true;
        tube.classList.add('shaking');

        var rolling = 0;
        var rollTimer = setInterval(function () {
            lotteryResult.textContent = luckList[Math.floor(Math.random() * luckList.length)];
            rolling++;
            if (rolling > 10) clearInterval(rollTimer);
        }, 60);

        api.act('DRAW', 'daily').then(function (result) {
            setTimeout(function () {
                clearInterval(rollTimer);
                tube.classList.remove('shaking');
                drawing = false;
                dailyText = result.text || dailyText;
                lotteryResult.textContent = dailyText || '今日福签已送达';
                setFortune(result.fortune, result.max);
                if (result.duplicate) {
                    toast('今天已经抽过签了，明天再来看看');
                } else if (result.awarded > 0) {
                    toast('福签已出：福气 +' + result.awarded);
                } else {
                    toast('福签已出，福气已经满格');
                }
                lockDraw();
            }, 900);
        }).catch(function (error) {
            clearInterval(rollTimer);
            tube.classList.remove('shaking');
            drawing = false;
            drawBtn.disabled = false;
            fail(error);
        });
    }

    function lightLantern(lamp, index) {
        if (lamp.classList.contains('on')) {
            toast('这盏灯已经亮了');
            return;
        }
        if (lamp.getAttribute('aria-busy') === 'true') return;
        lamp.setAttribute('aria-busy', 'true');

        api.act('LAMP', String(index + 1)).then(function (result) {
            lamp.removeAttribute('aria-busy');
            lamp.classList.add('on');
            lamp.setAttribute('aria-pressed', 'true');
            litCount = document.querySelectorAll('.lantern.on').length;
            lampCount.textContent = litCount;
            setFortune(result.fortune, result.max);

            if (result.duplicate) {
                toast('这盏灯今天已经点亮过了');
                return;
            }
            if (result.awarded > 0) {
                toast('福灯已点亮：福气 +' + result.awarded);
            } else {
                toast('福灯已点亮，福气已经满格');
            }
            if (litCount === 5) {
                setTimeout(function () {
                    toast('五盏福灯全亮，愿你所求皆有回响');
                }, 1600);
            }
        }).catch(function (error) {
            lamp.removeAttribute('aria-busy');
            fail(error);
        });
    }

    function flipCard(card, index) {
        if (card.classList.contains('flipped')) {
            card.classList.remove('flipped');
            return;
        }

        card.classList.add('flipped');
        api.act('FLIP', String(index + 1)).then(function (result) {
            setFortune(result.fortune, result.max);
            if (result.duplicate) {
                toast('这张牌今天已经翻过啦');
            } else if (result.awarded > 0) {
                toast('福俗卡已翻开：福气 +' + result.awarded);
            } else {
                toast('福俗卡已翻开，福气已经满格');
            }
        }).catch(fail);
    }

    function notice(text) {
        blessListDom.innerHTML = '';
        var empty = document.createElement('div');
        empty.className = 'bless-empty';
        empty.textContent = text;
        blessListDom.appendChild(empty);
    }

    function renderBless(list) {
        blessListDom.innerHTML = '';

        if (!list || !list.length) {
            notice('还没有人贴祝福，来写下第一条吧。');
            return;
        }

        list.forEach(function (item) {
            var div = document.createElement('div');
            div.className = 'bless-item';

            var who = document.createElement('div');
            who.className = 'who';

            var strong = document.createElement('strong');
            strong.textContent = item.username || '匿名同学';

            var time = document.createElement('span');
            time.textContent = item.createTime || '';

            who.appendChild(strong);
            who.appendChild(time);

            var p = document.createElement('p');
            p.textContent = item.content || '';

            div.appendChild(who);
            div.appendChild(p);
            blessListDom.appendChild(div);
        });
    }

    function loadBlessings() {
        notice('祝福加载中…');
        api.blessings(1, 20).then(renderBless).catch(function () {
            notice('祝福墙暂时没有加载出来，请稍后刷新试试。');
        });
    }

    function submitBlessing(event) {
        event.preventDefault();

        var username = document.getElementById('username').value.trim();
        var content = document.getElementById('blessContent').value.trim();

        if (!username || !content) {
            toast('昵称和祝福内容都要写上哦');
            return;
        }
        if (content.length > 80) {
            toast('祝福不能超过 80 个字');
            return;
        }

        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.textContent = '正在贴上…';
        }

        api.createBlessing(username, content).then(function (result) {
            blessForm.reset();
            setFortune(result.fortune, result.max);
            if (result.awarded > 0) {
                toast('祝福已贴上墙：福气 +' + result.awarded);
            } else if (result.duplicate) {
                toast('祝福已贴上墙，今天的心意已记录');
            } else {
                toast('祝福已贴上墙，福气已经满格');
            }
            loadBlessings();
        }).catch(fail).then(function () {
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.textContent = '把祝福贴上去 →';
            }
        });
    }

    if (!api) {
        toast('互动脚本加载失败，请刷新页面重试');
        if (blessListDom) {
            blessListDom.textContent = '祝福墙暂时无法加载。';
        }
        return;
    }

    drawBtn.addEventListener('click', drawFortune);
    lanterns.forEach(function (lamp, index) {
        lamp.addEventListener('click', function () {
            lightLantern(lamp, index);
        });
    });
    cards.forEach(function (card, index) {
        card.addEventListener('click', function () {
            flipCard(card, index);
        });
        card.addEventListener('keydown', function (event) {
            if (event.key === 'Enter' || event.key === ' ') {
                event.preventDefault();
                flipCard(card, index);
            }
        });
    });
    blessForm.addEventListener('submit', submitBlessing);

    restoreState();
    loadBlessings();
})();