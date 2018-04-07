if(!dojo._hasResource["tests.currency"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests.currency"] = true;
dojo.provide("tests.currency");

dojo.require("dojo.currency");

tests.register("tests.currency", 
	[
		{
			// Test formatting and parsing of currencies in various locales pre-built in dojo.cldr
			// NOTE: we can't set djConfig.extraLocale before bootstrapping unit tests, so directly
			// load resources here for specific locales:

			name: "currency",
			setUp: function(){
				var partLocaleList = ["en-us", "en-ca", "de-de"];
				for(var i = 0 ; i < partLocaleList.length; i ++){
					dojo.requireLocalization("dojo.cldr","currency",partLocaleList[i], "ar,be,bg,bo,ca,cs,ROOT,cy,da,de,el,el-polyton,en,en-au,en-ca,en-hk,en-ie,en-mt,en-nz,en-sg,en-us,es,es-ec,es-pr,et,fa,fa-af,fi,fr,fr-ca,ga,he,hi,hr,hu,is,it,ja,ko,lt,mk,ml,mt,nb,nl,nn,pa-arab,pa-pk,pl,pt,ro,ru,sh,sk,sl,sq,sr,sr-latn,sv,te,th,tr,uk,ur,zh,zh-hant,zh-hk,zh-mo,zh-tw");
					dojo.requireLocalization("dojo.cldr","number",partLocaleList[i], "af,af-na,ar,ar-dz,ar-ma,ar-qa,ar-sa,ar-sy,ar-tn,ar-ye,as,be,bg,bo,ca,cs,da,de,de-at,de-ch,de-de,de-li,el,el-cy,en,en-au,en-be,en-bw,en-bz,en-gb,en-ie,en-in,en-jm,en-mt,en-na,en-nz,en-sg,en-tt,en-us,en-us-posix,en-za,en-zw,eo,es,es-cl,es-do,es-ec,es-es,es-gt,es-hn,es-mx,es-ni,es-pa,es-pe,es-pr,es-py,es-sv,es-us,es-uy,es-ve,et,eu,fa,fa-af,fi,fo,fr,fr-be,fr-ca,fr-ch,fr-lu,ga,gl,gu,gv,haw,he,hi,hr,hu,hy,id,ii,is,it,it-ch,ja,ja-jp,kk,kl,km,kn,ko,ko-kr,kok,kw,lt,lv,mk,mr,ms,ms-bn,mt,nb,nl,nl-be,nn,ROOT,om,pa,pl,ps,pt,pt-pt,ro,ru,sh,sh-cs,sh-yu,sk,sl,so,sq,sr,sr-latn-cs,sr-latn-me,sr-latn-rs,sr-latn-yu,sv,sw,sw-ke,ta,te,th,ti,tr,uk,uz-af,uz-arab,vi,zh,zh-cn,zh-hant,zh-hant-hk,zh-hk,zh-mo,zh-tw");
				}
			},
			runTest: function(t){
				t.is("\u20ac123.45", dojo.currency.format(123.45, {currency: "EUR", locale: "en-us"}));
				t.is("$123.45", dojo.currency.format(123.45, {currency: "USD", locale: "en-us"}));
				t.is("$1,234.56", dojo.currency.format(1234.56, {currency: "USD", locale: "en-us"}));
				t.is("US$123.45", dojo.currency.format(123.45, {currency: "USD", locale: "en-ca"}));
				t.is("$123.45", dojo.currency.format(123.45, {currency: "CAD", locale: "en-ca"}));
				t.is("Can$123.45", dojo.currency.format(123.45, {currency: "CAD", locale: "en-us"}));
				t.is("123,45 \u20ac", dojo.currency.format(123.45, {currency: "EUR", locale: "de-de"}));
				t.is("1.234,56 \u20ac", dojo.currency.format(1234.56, {currency: "EUR", locale: "de-de"}));
				// There is no special currency symbol for ADP, so expect the ISO code instead
				t.is("ADP123", dojo.currency.format(123, {currency: "ADP", locale: "en-us"}));

				t.is(123.45, dojo.currency.parse("$123.45", {currency: "USD", locale: "en-us"}));
				t.is(1234.56, dojo.currency.parse("$1,234.56", {currency: "USD", locale: "en-us"}));
				t.is(123.45, dojo.currency.parse("123,45 \u20ac", {currency: "EUR", locale: "de-de"}));
				t.is(1234.56, dojo.currency.parse("1.234,56 \u20ac", {currency: "EUR", locale: "de-de"}));
				t.is(1234.56, dojo.currency.parse("1.234,56\u20ac", {currency: "EUR", locale: "de-de"}));

				t.is(1234, dojo.currency.parse("$1,234", {currency: "USD", locale: "en-us"}));
				t.is(1234, dojo.currency.parse("$1,234", {currency: "USD", fractional: false, locale: "en-us"}));
				t.t(isNaN(dojo.currency.parse("$1,234", {currency: "USD", fractional: true, locale: "en-us"})));
			}
		}
	]
);

}
