(function (global) {
    'use strict';

    var TIMEOUT = 8000;

    function request(path, options) {
        var opts = options || {};
        var headers = { 'Accept': 'application/json' };
        var init = {
            method: opts.method || 'GET',
            credentials: 'same-origin',
            headers: headers
        };

        if (opts.body !== undefined) {
            headers['Content-Type'] = 'application/json';
            init.body = JSON.stringify(opts.body);
        }

        var timer = null;
        if (typeof global.AbortController === 'function') {
            var controller = new global.AbortController();
            init.signal = controller.signal;
            timer = global.setTimeout(function () {
                controller.abort();
            }, TIMEOUT);
        }

        function clearTimer() {
            if (timer) global.clearTimeout(timer);
        }

        return global.fetch(path, init).then(function (response) {
            var type = response.headers.get('Content-Type') || '';
            var reading = type.indexOf('application/json') >= 0
                ? response.json()
                : response.text();

            return reading.then(function (data) {
                if (!response.ok) {
                    var message = data && data.message ? data.message : '请求失败，请稍后再试';
                    var error = new Error(message);
                    error.status = response.status;
                    throw error;
                }
                return data;
            });
        }, function (error) {
            if (error && error.name === 'AbortError') throw error;
            throw new Error('无法连接互动服务，请通过 http://localhost:8080 访问');
        }).then(function (data) {
            clearTimer();
            return data;
        }, function (error) {
            clearTimer();
            if (error && error.name === 'AbortError') {
                throw new Error('请求超时，请稍后再试');
            }
            throw error;
        });
    }

    global.FZApi = {
        fortuneState: function () {
            return request('/api/fortune');
        },
        dailyFortune: function () {
            return request('/api/fortune/daily');
        },
        act: function (action, ref) {
            return request('/api/fortune/actions', {
                method: 'POST',
                body: { action: action, ref: ref || '' }
            });
        },
        blessings: function (page, size) {
            return request('/api/blessings?page=' + (page || 1) + '&size=' + (size || 20));
        },
        createBlessing: function (nickname, content) {
            return request('/api/blessings', {
                method: 'POST',
                body: { nickname: nickname, content: content }
            });
        }
    };
})(window);