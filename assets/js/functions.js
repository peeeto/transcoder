// GUI functions
function initBCryptRounds(max, panel) {
    for (jj = 4; jj < max; jj++) {
        panel.append(new Option(jj, jj));
    }
}

$(function () {
    initBCryptRounds(32, $("#bcryptRounds"))
});

$(function () {
    var selectAll = function () {
        this.select();
    };
    $(document).one('click', 'input[type=text]', selectAll);
    $(document).one('click', 'textarea', selectAll);
});


function getPanel() {
    return $('#panel');
}
// computation functions

var scrypt = scrypt_module_factory();

var bcrypt = new bCrypt();

function xmlFormat(str) {
    try {
        return vkbeautify.xml(str);
    } catch (error) {
        var msg = 'Not XML: ' + error;
        alert(msg);
        console.log(msg);
    }
}

function jsonFormat(str) {
    try {
        str = str.replace(/([a-z][^:]*)(?=\s*:)/g, '"$1"');
        return JSON.stringify(JSON.parse(str), null, 4);
    } catch (error) {
        var msg = 'Not JSON: ' + error;
        alert(msg);
        console.log(msg);
    }
}

function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

function isInt(value) {
    return !isNaN(value) && parseInt(Number(value)) == value && !isNaN(parseInt(value, 10));
}

function encode(element, hashFunc, toStringFunc) {
    var str = element.val();
    //var parsed = CryptoJS.enc.Utf8.parse(str);
    var parsed = str;
    var hash = hashFunc(parsed);
    var result = hash.toString(toStringFunc);
    element.val(result);
    return result;
}

function prepareUtf16String(str) {
    return encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function (match, p1) {
        return String.fromCharCode('0x' + p1);
    });
}

function stringToMillis(date) {
    if (!!date) {
        var m = moment(date, 'YYYY-MM-DD HH:mm:ss.SSS Z');
        if (m.isValid()) {
            return m.valueOf();
        }
    }
    return new Date().getTime();
}

function millisToString(date) {
    if (!isInt(date)) {
        date = new Date().getTime();
    }
    return moment.utc(parseInt(date)).format('YYYY-MM-DD HH:mm:ss.SSS Z');
}

function bc(passwordToCheck, toHash, rounds) {
    if (passwordToCheck.length <= 0) {
        var salt = bcrypt.gensalt(parseInt(rounds));
        bcrypt.hashpw(toHash, salt, function (hash) {
            getPanel().val(hash);
        });
    } else {
        try {
            bcrypt.checkpw(passwordToCheck, toHash, function (res) {
                alert("Password and Hash are: " + (!!res ? "OK" : "NOT ok"));
            });
        } catch (error) {
            alert("Hash does not contain Salt to check");
        }
    }
}

function sc(passwordToCheck, toHash, salt, n, r, p) {
    if (passwordToCheck.trim().length <= 0) {
        var hash = scrypt.crypto_scrypt(
            scrypt.encode_utf8(toHash),
            scrypt.encode_utf8(salt),
            parseInt(n), parseInt(r), parseInt(p), 32);
        var params = (((Math.log2(n) << 16) | (r << 8) | p)).toString(16);
        var slt = btoa(salt);
        var derived = btoa(String.fromCharCode.apply(null, hash));
        return "$s0$" + params + "$" + slt + "$" + derived;
    } else {
        try {
            var parts = toHash.split("\$");
            if (parts.length != 5 || parts[1] !== 's0') {
                alert("Invalid format of validated string")
            } else {
                params = parseInt(parts[2], 16);
                salt = atob(parts[3]);
                var passKey = parts[4];
                n = Math.pow(2, params >> 16 & 0xffff);
                r = params >> 8 & 0xff;
                p = params & 0xff;
                hash = scrypt.crypto_scrypt(
                    scrypt.encode_utf8(passwordToCheck),
                    scrypt.encode_utf8(salt),
                    n, r, p, 32);
                derived = btoa(String.fromCharCode.apply(null, hash));
                var res = (passKey === derived);
                alert("Password and Hash are: " + (!!res ? "OK" : "NOT ok"));
            }
        } catch (error) {
            alert("Hash does not contain Salt to check");
        }
    }
}

function base64Encode(str) {
    return btoa(unescape(encodeURIComponent(str)));
}

function base64Decode(str) {
    return decodeURIComponent(escape(atob(str)));
}
