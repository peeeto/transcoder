function Mock(str, selectionStart, selectionEnd) {
    this.value = str;
    this.selectionStart = selectionStart === undefined ? 0 : selectionStart;
    this.selectionEnd = selectionEnd === undefined ? 0 : selectionEnd;

    this.val = function (str) {
        if (str !== undefined) this.value = str;
        return this.value;
    };
    this.get = function () {
        return this;
    };
}

function assertHashesEqual(input, expected, hashFunc, toStringFunc, assert) {
    var element = new Mock(input);
    encode(element, hashFunc, toStringFunc);
    assert.equal(element.val(), expected);
}

/**
 echo -n '' | openssl md5
 echo -n 'admin' | openssl md5
 echo -n ' admin '' | openssl md5
 chcp.com 65001; echo -n '✓ à la mode' | openssl md5
 */
QUnit.test('md5Hex', function (assert) {
    var hashFunc = CryptoJS.MD5;
    var toStringFunc = CryptoJS.enc.Hex;
    assertHashesEqual('', 'd41d8cd98f00b204e9800998ecf8427e', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', '21232f297a57a5a743894a0e4a801fc3', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', '94e29491ed800dba5927e35a9272b138', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '060ee7b9651684cf44b4d4c67ae96231', hashFunc, toStringFunc, assert);
});
QUnit.test('sha1Hex', function (assert) {
    var hashFunc = CryptoJS.SHA1;
    var toStringFunc = CryptoJS.enc.Hex;
    assertHashesEqual('', 'da39a3ee5e6b4b0d3255bfef95601890afd80709', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', '2c1719fa1481aa04599db3fc786d3bbdaad1aa53', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '8dee91e1a13f7797b9183f79aeca7b94865d501b', hashFunc, toStringFunc, assert);
});
QUnit.test('sha256Hex', function (assert) {
    var hashFunc = CryptoJS.SHA256;
    var toStringFunc = CryptoJS.enc.Hex;
    assertHashesEqual('', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', 'dd4d593d726dc2ce9becdf82c9d28eda488339e03c9262b4df54eb73590d445f', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '7df11e628347633af3d92d3f1c704d09998b876421df4772847f5b02081f7cd0', hashFunc, toStringFunc, assert);
});
QUnit.test('sha512Hex', function (assert) {
    var hashFunc = CryptoJS.SHA512;
    var toStringFunc = CryptoJS.enc.Hex;
    assertHashesEqual('', 'cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', '5466895dd67f51cd95214ad8c156d0be31071631105123e50cd121ce26d36adb53fc770965623f09a1f1858493876a912ff096791a73ddcbca368d8175e5e2b4', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '6fcbeb42366f872b918d1868480691a997238ec30dd0de34e1b7e0b0ff5512252c16b9e6519b2a3f51ac88fa827af725886134a08f36a67fd271d0350d793074', hashFunc, toStringFunc, assert);
});
QUnit.test('ripemd160Hex', function (assert) {
    var hashFunc = CryptoJS.RIPEMD160;
    var toStringFunc = CryptoJS.enc.Hex;
    assertHashesEqual('', '9c1185a5c5e9fc54612808977ee8f548b2258d31', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', '7dd12f3a9afa0282a575b8ef99dea2a0c1becb51', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', '1b19e8c7215d137acc9cfe053521be6aab2bd3b3', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '9310208ebfd0765427a69737dbbd460797d0463c', hashFunc, toStringFunc, assert);
});

/**
 echo -n '' | openssl md5 -binary | openssl base64
 echo -n 'admin' | openssl md5 -binary | openssl base64
 echo -n ' admin '' | openssl md5 -binary | openssl base64
 chcp.com 65001; echo -n '✓ à la mode' | openssl md5 -binary | openssl base64
 */
QUnit.test('md5Base64', function (assert) {
    assertHashesEqual('', '1B2M2Y8AsgTpgAmY7PhCfg==', CryptoJS.MD5, CryptoJS.enc.Base64, assert);
    assertHashesEqual('admin', 'ISMvKXpXpadDiUoOSoAfww==', CryptoJS.MD5, CryptoJS.enc.Base64, assert);
    assertHashesEqual(' admin ', 'lOKUke2ADbpZJ+NaknKxOA==', CryptoJS.MD5, CryptoJS.enc.Base64, assert);
    assertHashesEqual('✓ à la mode', 'Bg7nuWUWhM9EtNTGeuliMQ==', CryptoJS.MD5, CryptoJS.enc.Base64, assert);
});
QUnit.test('sha1Base64', function (assert) {
    var hashFunc = CryptoJS.SHA1;
    var toStringFunc = CryptoJS.enc.Base64;
    assertHashesEqual('', '2jmj7l5rSw0yVb/vlWAYkK/YBwk=', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', '0DPiKuNIrrVmD8IUCuw1hQxNqZc=', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', 'LBcZ+hSBqgRZnbP8eG07varRqlM=', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', 'je6R4aE/d5e5GD95rsp7lIZdUBs=', hashFunc, toStringFunc, assert);
});
QUnit.test('sha256Base64', function (assert) {
    var hashFunc = CryptoJS.SHA256;
    var toStringFunc = CryptoJS.enc.Base64;
    assertHashesEqual('', '47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', '3U1ZPXJtws6b7N+CydKO2kiDOeA8kmK031Trc1kNRF8=', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', 'ffEeYoNHYzrz2S0/HHBNCZmLh2Qh30dyhH9bAggffNA=', hashFunc, toStringFunc, assert);
});
QUnit.test('sha512Base64', function (assert) {
    var hashFunc = CryptoJS.SHA512;
    var toStringFunc = CryptoJS.enc.Base64;
    assertHashesEqual('', 'z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg==', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'x61Ey612Kl2gpFL56FT9weDnpSo4AV8j8+qx2AuTHdRyY036xxzTTrw10Wq3+4qQyB+XURPWx1ONxp3Y3pB37A==', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', 'VGaJXdZ/Uc2VIUrYwVbQvjEHFjEQUSPlDNEhzibTattT/HcJZWI/CaHxhYSTh2qRL/CWeRpz3cvKNo2BdeXitA==', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', 'b8vrQjZvhyuRjRhoSAaRqZcjjsMN0N404bfgsP9VEiUsFrnmUZsqP1GsiPqCevcliGE0oI82pn/ScdA1DXkwdA==', hashFunc, toStringFunc, assert);
});
QUnit.test('ripemd160Base64', function (assert) {
    var hashFunc = CryptoJS.RIPEMD160;
    var toStringFunc = CryptoJS.enc.Base64;
    assertHashesEqual('', 'nBGFpcXp/FRhKAiXfuj1SLIljTE=', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'fdEvOpr6AoKldbjvmd6ioMG+y1E=', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', 'GxnoxyFdE3rMnP4FNSG+aqsr07M=', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', 'kxAgjr/QdlQnppc3271GB5fQRjw=', hashFunc, toStringFunc, assert);
});
QUnit.test('urlEncode', function (assert) {
    var hashFunc = encodeURIComponent;
    var toStringFunc = CryptoJS.enc.Utf8;
    assertHashesEqual('', '', hashFunc, toStringFunc, assert);
    assertHashesEqual(' a a ', '%20a%20a%20', hashFunc, toStringFunc, assert);
    assertHashesEqual('a a', 'a%20a', hashFunc, toStringFunc, assert);
    assertHashesEqual('http://google.com', 'http%3A%2F%2Fgoogle.com', hashFunc, toStringFunc, assert);

    var testData = "http://www.example.com/string with + and ? and & and spaces";
    assert.equal(encodeURI(testData), 'http://www.example.com/string%20with%20+%20and%20?%20and%20&%20and%20spaces');
    assert.equal(encodeURIComponent(testData), 'http%3A%2F%2Fwww.example.com%2Fstring%20with%20%2B%20and%20%3F%20and%20%26%20and%20spaces');
});
QUnit.test('urlDecode', function (assert) {
    var hashFunc = decodeURIComponent;
    var toStringFunc = CryptoJS.enc.Utf8;
    assertHashesEqual('', '', hashFunc, toStringFunc, assert);
    assertHashesEqual('%20a%20a%20', ' a a ', hashFunc, toStringFunc, assert);
    assertHashesEqual('a%20a', 'a a', hashFunc, toStringFunc, assert);
    assertHashesEqual('http%3A%2F%2Fgoogle.com', 'http://google.com', hashFunc, toStringFunc, assert);
});

QUnit.test('base64Encode', function (assert) {
    var hashFunc = base64Encode;
    var toStringFunc = CryptoJS.enc.Utf8;
    assertHashesEqual('', '', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'YWRtaW4=', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', 'IGFkbWluIA==', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '4pyTIMOgIGxhIG1vZGU=', hashFunc, toStringFunc, assert);
});
QUnit.test('base64Decode', function (assert) {
    var hashFunc = base64Decode;
    var toStringFunc = CryptoJS.enc.Utf8;
    assertHashesEqual('', '', hashFunc, toStringFunc, assert);
    assertHashesEqual('YWRtaW4=', 'admin', hashFunc, toStringFunc, assert);
    assertHashesEqual('IGFkbWluIA==', ' admin ', hashFunc, toStringFunc, assert);
    assertHashesEqual('4pyTIMOgIGxhIG1vZGU=', '✓ à la mode', hashFunc, toStringFunc, assert);
});

QUnit.test('base64EncodeDecodeWithStartSelected', function (assert) {
    var element = new Mock('asd', 0, 1);
    encode(element, base64Encode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'YQ==' + 'sd');
    assert.equal(element.selectionStart, 0);
    assert.equal(element.selectionEnd, 4);

    encode(element, base64Decode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'asd');
});
QUnit.test('base64EncodeDecodeWithEndSelected', function (assert) {
    var element = new Mock('asd', 2, 3);
    encode(element, base64Encode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'as' + 'ZA==');
    assert.equal(element.selectionStart, 2);
    assert.equal(element.selectionEnd, 6);

    encode(element, base64Decode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'asd');
});
QUnit.test('base64EncodeDecodeWithMiddleSelected', function (assert) {
    var element = new Mock('asd', 1, 2);
    encode(element, base64Encode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'a' + 'cw==' + 'd');
    assert.equal(element.selectionStart, 1);
    assert.equal(element.selectionEnd, 5);

    encode(element, base64Decode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'asd');
});
QUnit.test('base64EncodeDecodeWithAllSelected', function (assert) {
    var element = new Mock('asd', 0, 3);
    encode(element, base64Encode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'YXNk');
    assert.equal(element.selectionStart, 0);
    assert.equal(element.selectionEnd, 4);

    encode(element, base64Decode, CryptoJS.enc.Utf8);
    assert.equal(element.val(), 'asd');
});
QUnit.test('base32Encode', function (assert) {
    var hashFunc = CryptoJS.enc.Utf8.parse;
    var toStringFunc = CryptoJS.enc.Base32;
    assertHashesEqual('', '', hashFunc, toStringFunc, assert);
    assertHashesEqual('admin', 'MFSG22LO', hashFunc, toStringFunc, assert);
    assertHashesEqual(' admin ', 'EBQWI3LJNYQA====', hashFunc, toStringFunc, assert);
    assertHashesEqual('✓ à la mode', '4KOJGIGDUAQGYYJANVXWIZI=', hashFunc, toStringFunc, assert);
});
QUnit.test('base32Decode', function (assert) {
    var hashFunc = CryptoJS.enc.Base32.parse;
    var toStringFunc = CryptoJS.enc.Utf8;
    assertHashesEqual('', '', hashFunc, toStringFunc, assert);
    assertHashesEqual('MFSG22LO', 'admin', hashFunc, toStringFunc, assert);
    assertHashesEqual('EBQWI3LJNYQA====', ' admin ', hashFunc, toStringFunc, assert);
    assertHashesEqual('4KOJGIGDUAQGYYJANVXWIZI=', '✓ à la mode', hashFunc, toStringFunc, assert);
});

QUnit.test('stringToMillis', function (assert) {
    assert.equal(stringToMillis('2015-11-08 21:05:34.670 +00:00'), '1447016734670');
    assert.equal(stringToMillis('2015-11-08 21:06:25.711 +00:00'), '1447016785711');
});
QUnit.test('millisToString', function (assert) {
    assert.equal(millisToString('1447016734670'), '2015-11-08 21:05:34.670 +00:00');
    assert.equal(millisToString('1447016785711'), '2015-11-08 21:06:25.711 +00:00');
});
/**
 * echo -n "admin" | openssl md5 -binary|base64 ; ISMvKXpXpadDiUoOSoAfww==
 */
QUnit.test('base64ToHex', function (assert) {
    var hashFunc = function (str) {
        return CryptoJS.enc.Base64.parse(str);
    };
    var toStringFunc = CryptoJS.enc.Hex;
    assertHashesEqual('ISMvKXpXpadDiUoOSoAfww==', '21232f297a57a5a743894a0e4a801fc3', hashFunc, toStringFunc, assert);
});
/**
 *  echo -n "ISMvKXpXpadDiUoOSoAfww==" | base64 -d | xxd -ps ; 21232f297a57a5a743894a0e4a801fc3
 *  echo -n "admin" | openssl md5 ; 21232f297a57a5a743894a0e4a801fc3
 */
QUnit.test('hexToBase64', function (assert) {
    var hashFunc = CryptoJS.enc.Hex.parse;
    var toStringFunc = CryptoJS.enc.Base64;
    assertHashesEqual('21232f297a57a5a743894a0e4a801fc3', 'ISMvKXpXpadDiUoOSoAfww==', hashFunc, toStringFunc, assert);
});

QUnit.test('jsonFormat', function (assert) {
    assert.equal(jsonFormat('{"GlossDef": {"para": "A meta-mar","GlossSeeAlso": ["GML", "XML"]},"GlossSee": true}'),
        '{\n' +
        '    "GlossDef": {\n' +
        '        "para": "A meta-mar",\n' +
        '        "GlossSeeAlso": [\n' +
        '            "GML",\n' +
        '            "XML"\n' +
        '        ]\n' +
        '    },\n' +
        '    "GlossSee": true\n' +
        '}'
    );
    assert.equal(jsonFormat("{'GlossDef': {'para': 'A meta-mar','GlossSeeAlso': ['GML', 'XML']},'GlossSee': true}"),
        "{\n" +
        "    'GlossDef': {\n" +
        "        'para': 'A meta-mar',\n" +
        "        'GlossSeeAlso': [\n" +
        "            'GML',\n" +
        "            'XML'\n" +
        "        ]\n" +
        "    },\n" +
        "    'GlossSee': true\n" +
        "}"
    );
});
QUnit.test('toUpperCase', function (assert) {
    assert.equal(toUpperCase(''), '');
    assert.equal(toUpperCase('až'), 'AŽ');
});
QUnit.test('toLowerCase', function (assert) {
    assert.equal(toLowerCase(''), '');
    assert.equal(toLowerCase('AŽ'), 'až');
});
QUnit.test('scrypt', function (assert) {
    assert.equal(sc('', 'password', 'randomSalt', 1024, 8, 1), '$s0$a0801$cmFuZG9tU2FsdA==$4BgX8flnz7UGOZ30L5nB7Y1aMJl2pg8JSpRxMhjwxrs=');
    assert.equal(sc('', 'a', 'randomSalt', 1024, 8, 1), '$s0$a0801$cmFuZG9tU2FsdA==$NmXr19xOOkw4RHDXJ1QsiYmwClLpdnCWEeHEVAbT10o=');
    assert.equal(sc('', '', 'randomSalt', 1024, 8, 1), '$s0$a0801$cmFuZG9tU2FsdA==$lKzthd2QyCK41w5ErdR1XkDDW1r69gganjHLADgw2Ec=');
    assert.equal(sc('', '64d04051-e48a-00fd-d996-82b9fd5a9ca2', '64d04051', 1024, 8, 1), '$s0$a0801$NjRkMDQwNTE=$OpPyz5onfpBVczPbmHbQvKMtTbiH9+U+CQrHwpIUHWY=');
    assert.equal(sc('', '64d04051-e48a-00fd-d996-82b9fd5a9ca2', '64d04051', 2048, 16, 2), '$s0$b1002$NjRkMDQwNTE=$ckWl5OWrgTlwwR6dhEbs0OZff8FnsUPnS9SL4xCF3Xs=');
});
function assertHmacEqual(payload, hmacFunction, secret, toStringFunc, assert, expected) {
    var hmacFunc = function (str) {
        return hmacFunction(str, secret);
    };
    assertHashesEqual(payload, expected, hmacFunc, toStringFunc, assert);
}

/**
 * echo -n "payload" | openssl md5 -hmac "secret"
 */
QUnit.test('hmacMd5Hex', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacMD5, 'secret', CryptoJS.enc.Hex, assert, '4f967408b96fb0e40180074062022698');
});
/**
 * echo -n "payload" | openssl sha1 -hmac "secret"
 */
QUnit.test('hmacSha1Hex', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacSHA1, 'secret', CryptoJS.enc.Hex, assert, 'f75efc0f29bf50c23f99b30b86f7c78fdaf5f11d');
});
/**
 * echo -n "payload" | openssl sha256 -hmac "secret"
 */
QUnit.test('hmacSha256Hex', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacSHA256, 'secret', CryptoJS.enc.Hex, assert, 'b82fcb791acec57859b989b430a826488ce2e479fdf92326bd0a2e8375a42ba4');
});
/**
 * echo -n "payload" | openssl sha512 -hmac "secret"
 */
QUnit.test('hmacSha512Hex', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacSHA512, 'secret', CryptoJS.enc.Hex, assert, '291ddaaa23cafa3aaae1c9755391f4bef35bbdbcb92739a5618a5c896f6520d2b0d28d2d2987dac97479e31214a51d96cfceafa28e46a4f961b63c46352a189e');
});

/**
 * echo -n "payload" | openssl md5 -hmac "secret" -binary | openssl base64
 */
QUnit.test('hmacMd5Base64', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacMD5, 'secret', CryptoJS.enc.Base64, assert, 'T5Z0CLlvsOQBgAdAYgImmA==');
});
/**
 * echo -n "payload" | openssl sha1 -hmac "secret" -binary | openssl base64
 */
QUnit.test('hmacSha1Base64', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacSHA1, 'secret', CryptoJS.enc.Base64, assert, '9178Dym/UMI/mbMLhvfHj9r18R0=');
});
/**
 * echo -n "payload" | openssl sha256 -hmac "secret" -binary | openssl base64
 */
QUnit.test('hmacSha256Base64', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacSHA256, 'secret', CryptoJS.enc.Base64, assert, 'uC/LeRrOxXhZuYm0MKgmSIzi5Hn9+SMmvQoug3WkK6Q=');
});
/**
 * echo -n "payload" | openssl sha512 -hmac "secret" -binary | openssl base64
 */
QUnit.test('hmacSha512Base64', function (assert) {
    assertHmacEqual('payload', CryptoJS.HmacSHA512, 'secret', CryptoJS.enc.Base64, assert, 'KR3aqiPK+jqq4cl1U5H0vvNbvby5JzmlYYpciW9lINKw0o0tKYfayXR54xIUpR2Wz86voo5GpPlhtjxGNSoYng==');
});
//TODO make testable and test
//QUnit.test('bcrypt', function (assert) {
//    var actual = bc('', 'admin', 4);
//    assert.equal(bc(actual, 'admin', 0), true);
//});
//QUnit.test('xmlFormat', function (assert) {
//assert.equal(1, 2, '');
//});

QUnit.test('wowzaPrepareSecureUrl - SHA256', function (assert) {
    var wowzaViewerIp = '192.168.203.62';
    var wowzaContentPath = 'vod-demo/mp4:sample.mp4';
    var wowzaContentURL = "http://192.168.203.62:1935/" + wowzaContentPath + "/playlist.m3u8";
    var wowzaSecureToken = '65ce098c8c397e54';
    var wowzaTokenPrefix = 'wowzatoken';
    var wowzaCustomParameter = wowzaTokenPrefix + 'CustomParameter=myParameter';
    var wowzaSecureTokenStartTimeSeconds = wowzaTokenPrefix + 'starttime=1452672928';
    var wowzaSecureTokenEndTimeSeconds = wowzaTokenPrefix + 'endtime=14526729287200';
    var wowzaHashFunc='SHA256';

    assert.equal(wowzaPrepareSecureUrl(wowzaViewerIp, wowzaContentPath, wowzaContentURL, wowzaSecureToken, wowzaTokenPrefix, wowzaCustomParameter, wowzaSecureTokenStartTimeSeconds, wowzaSecureTokenEndTimeSeconds, wowzaHashFunc),
        'http://192.168.203.62:1935/vod-demo/mp4:sample.mp4/playlist.m3u8?wowzatokenstarttime=1452672928&wowzatokenendtime=14526729287200&wowzatokenCustomParameter=myParameter&wowzatokenhash=c119qiGX92fXNsc7VSTJICOVCeaH4rLVCf4777ySSc4=');

    assert.equal(wowzaPrepareSecureUrl(' ', wowzaContentPath, wowzaContentURL, wowzaSecureToken, wowzaTokenPrefix, wowzaCustomParameter, wowzaSecureTokenStartTimeSeconds, wowzaSecureTokenEndTimeSeconds, wowzaHashFunc),
        'http://192.168.203.62:1935/vod-demo/mp4:sample.mp4/playlist.m3u8?wowzatokenstarttime=1452672928&wowzatokenendtime=14526729287200&wowzatokenCustomParameter=myParameter&wowzatokenhash=e0NfLr7sgHYDOntx8LiRdXEoXkNjFKGYRDAmSYyH8Fo=');
});

QUnit.test('wowzaPrepareSecureUrl - SHA384', function (assert) {
    var wowzaViewerIp = '192.168.203.62';
    var wowzaContentPath = 'vod-demo/mp4:sample.mp4';
    var wowzaContentURL = "http://192.168.203.62:1935/" + wowzaContentPath + "/playlist.m3u8";
    var wowzaSecureToken = '65ce098c8c397e54';
    var wowzaTokenPrefix = 'wowzatoken';
    var wowzaCustomParameter = wowzaTokenPrefix + 'CustomParameter=myParameter';
    var wowzaSecureTokenStartTimeSeconds = wowzaTokenPrefix + 'starttime=1452672928';
    var wowzaSecureTokenEndTimeSeconds = wowzaTokenPrefix + 'endtime=14526729287200';
    var wowzaHashFunc='SHA384';

    assert.equal(wowzaPrepareSecureUrl(wowzaViewerIp, wowzaContentPath, wowzaContentURL, wowzaSecureToken, wowzaTokenPrefix, wowzaCustomParameter, wowzaSecureTokenStartTimeSeconds, wowzaSecureTokenEndTimeSeconds, wowzaHashFunc),
        'http://192.168.203.62:1935/vod-demo/mp4:sample.mp4/playlist.m3u8?wowzatokenstarttime=1452672928&wowzatokenendtime=14526729287200&wowzatokenCustomParameter=myParameter&wowzatokenhash=hY8USwkcJl1fH0xFwg4uNoAVOTfqZsSL9P7DmimKDCW8AS5nPKIDky1cnhKOOrhG');

    assert.equal(wowzaPrepareSecureUrl(' ', wowzaContentPath, wowzaContentURL, wowzaSecureToken, wowzaTokenPrefix, wowzaCustomParameter, wowzaSecureTokenStartTimeSeconds, wowzaSecureTokenEndTimeSeconds, wowzaHashFunc),
        'http://192.168.203.62:1935/vod-demo/mp4:sample.mp4/playlist.m3u8?wowzatokenstarttime=1452672928&wowzatokenendtime=14526729287200&wowzatokenCustomParameter=myParameter&wowzatokenhash=6j3JWsRZRi5Z0ZFr3o5P1vZaOlbhpIgk5WY/35wMRmhDVZB1TW1WxThIMAx0xfv/');
});

QUnit.test('wowzaPrepareSecureUrl - SHA512', function (assert) {
    var wowzaViewerIp = '192.168.203.62';
    var wowzaContentPath = 'vod-demo/mp4:sample.mp4';
    var wowzaContentURL = "http://192.168.203.62:1935/" + wowzaContentPath + "/playlist.m3u8";
    var wowzaSecureToken = '65ce098c8c397e54';
    var wowzaTokenPrefix = 'wowzatoken';
    var wowzaCustomParameter = wowzaTokenPrefix + 'CustomParameter=myParameter';
    var wowzaSecureTokenStartTimeSeconds = wowzaTokenPrefix + 'starttime=1452672928';
    var wowzaSecureTokenEndTimeSeconds = wowzaTokenPrefix + 'endtime=14526729287200';
    var wowzaHashFunc='SHA512';

    assert.equal(wowzaPrepareSecureUrl(wowzaViewerIp, wowzaContentPath, wowzaContentURL, wowzaSecureToken, wowzaTokenPrefix, wowzaCustomParameter, wowzaSecureTokenStartTimeSeconds, wowzaSecureTokenEndTimeSeconds, wowzaHashFunc),
        'http://192.168.203.62:1935/vod-demo/mp4:sample.mp4/playlist.m3u8?wowzatokenstarttime=1452672928&wowzatokenendtime=14526729287200&wowzatokenCustomParameter=myParameter&wowzatokenhash=g8EHF+wG3JgNEOG/MXGlVxbCX15dzQPmw1KKORvDGis7AZwoiPFSXTtW0Lag2YJM/8K1182J0CzcrRtqsKiyCQ==');

    assert.equal(wowzaPrepareSecureUrl(' ', wowzaContentPath, wowzaContentURL, wowzaSecureToken, wowzaTokenPrefix, wowzaCustomParameter, wowzaSecureTokenStartTimeSeconds, wowzaSecureTokenEndTimeSeconds, wowzaHashFunc),
        'http://192.168.203.62:1935/vod-demo/mp4:sample.mp4/playlist.m3u8?wowzatokenstarttime=1452672928&wowzatokenendtime=14526729287200&wowzatokenCustomParameter=myParameter&wowzatokenhash=WQ1AR9IOH4ftol1++ye416vMqXq3r/8rrl+45GL4hNTUbGG3H9DM7GLTniBOEY8Sj7PD8eDDetucBHDXnSZRUw==');
});