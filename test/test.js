QUnit.test("hello testing", function (assert) {
    assert.equal(testing(), "asdf", "Passed!");
});

QUnit.test("stringToMillis", function (assert) {
    assert.equal(stringToMillis("2015-11-08 21:05:34.670 +00:00"), "1447016734670");
    assert.equal(stringToMillis("2015-11-08 21:06:25.711 +00:00"), "1447016785711");
});

QUnit.test("millisToString", function (assert) {
    assert.equal(millisToString("1447016734670"), "2015-11-08 21:05:34.670 +00:00");
    assert.equal(millisToString("1447016785711"), "2015-11-08 21:06:25.711 +00:00");
});

QUnit.test("test scrypt", function (assert) {
    assert.equal(sc('', "password", "randomSalt", 1024, 8, 1), "$s0$a0801$cmFuZG9tU2FsdA==$4BgX8flnz7UGOZ30L5nB7Y1aMJl2pg8JSpRxMhjwxrs=");
    assert.equal(sc('', "a", "randomSalt", 1024, 8, 1), "$s0$a0801$cmFuZG9tU2FsdA==$NmXr19xOOkw4RHDXJ1QsiYmwClLpdnCWEeHEVAbT10o=");
    assert.equal(sc('', "", "randomSalt", 1024, 8, 1), "$s0$a0801$cmFuZG9tU2FsdA==$lKzthd2QyCK41w5ErdR1XkDDW1r69gganjHLADgw2Ec=");
    assert.equal(sc('', "64d04051-e48a-00fd-d996-82b9fd5a9ca2", "64d04051", 1024, 8, 1), "$s0$a0801$NjRkMDQwNTE=$OpPyz5onfpBVczPbmHbQvKMtTbiH9+U+CQrHwpIUHWY=");
    assert.equal(sc('', "64d04051-e48a-00fd-d996-82b9fd5a9ca2", "64d04051", 2048, 16, 2), "$s0$b1002$NjRkMDQwNTE=$ckWl5OWrgTlwwR6dhEbs0OZff8FnsUPnS9SL4xCF3Xs=");
});

