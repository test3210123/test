import cheerio from "assets://js/lib/cheerio.min.js";
import "assets://js/lib/crypto-js.js";
import 模板 from "./模板.js";
import {
	gbkTool
}
from "./gbk.js";

function parseAnticode(code, uid, streamname) {
    const q = {};
    const pairs = code.split('&');
    for (const pair of pairs) {
      const [key, value] = pair.split('=');
      q[key] = [decodeURIComponent(value)];
    }
    q.ver = ["1"];
    q.sv = ["2110211124"];

    q.seqid = [String(Number.parseInt(uid) + new Date().getTime())];
    console.log("seqid" + q.seqid);

    q.uid = [uid];
    q.uuid = [String(newUuid())];
    console.log("uuid" + q.uuid);

    const ss = CryptoJS.MD5(`${q.seqid[0]}|${q.ctype[0]}|${q.t[0]}`).toString(CryptoJS.enc.Hex);
    console.log("ss" + ss);

    q.fm[0] = CryptoJS.enc.Base64.parse(q.fm[0]).toString(CryptoJS.enc.Utf8)
      .replace("$0", q.uid[0])
      .replace("$1", streamname)
      .replace("$2", ss)
      .replace("$3", q.wsTime[0]);

    q.wsSecret[0] = CryptoJS.MD5(q.fm[0]).toString(CryptoJS.enc.Hex);
    console.log("wsSecret" + q.wsSecret);

    delete q.fm;
    if ("txyp" in q) {
      delete q.txyp;
    }
//    console.log("home--------a0");

    const queryString = Object.entries(q)
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value[0])}`)
      .join("&");

//    console.log("home--------a1");
    return queryString;
}

function newUuid() {
  console.log("创建新 uuid");

  const now = new Date().getTime();
  const rand = Math.floor(Math.random() * 1000) | 0;
  return ((now % 10000000000) * 1000 + rand) % 4294967295;
}


function init(ext) {
	console.log("init");
	console.log("init.ext=" + ext);
}

function home(filter) {
	console.log("home");
//  try {
//    console.log("home--------0");
//    var a = parseAnticode("wsSecret=7d66653d9e4264aed880a5a1f928bb34&wsTime=683aa624&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mp&txyp=o%3An4%3B&fs=bgct&t=102", "1469595604708", "1394575534-1394575534-5989656310331736064-2789274524-10057-A-0-1");
//    console.log("home--------1");
//    console.log(a);
//    console.log("home--------2");
//    return a;
//  } catch(e) {
//    console.log("home执行失败:" + e.message)
//  }
	return null;
}

function homeVod(params) {
	console.log("homeVod");
	return null
}

function category(tid, pg, filter, extend) {
  console.log("category");
	return null;
}

function detail(vod_url) {
  console.log("detail");
	return null;
}

function play(antiCode, uid, streamname) {
  console.log("play");
  try {
    console.log("--------play.0");
    console.log("--------play.antiCode="+antiCode);
    console.log("--------play.uid="+uid);
    console.log("--------play.streamname="+streamname);
//    console.log("--------play.2");
    var program = parseAnticode(antiCode, uid, streamname[0]);
    console.log("--------play.program="+program);
//    console.log("--------play.3");
    return program;
  } catch(e) {
    console.log("play执行失败:" + e.message)
  }
	return null;
}

function search(wd, quick, pg) {
  console.log("search");
	return null;
}

function proxy(params) {
  console.log("proxy");
	return null;
}

function sniffer() {
  console.log("sniffer");
	return null;
}

function isVideo(url) {
  console.log("isVideo");
	return null;
}

function DRPY() {
	return {
		init: init,
		home: home,
		homeVod: homeVod,
		category: category,
		detail: detail,
		play: play,
		search: search,
		proxy: proxy,
		sniffer: sniffer,
		isVideo: isVideo
	}
}

export default {
  init: init,
  home: home,
  homeVod: homeVod,
  category: category,
  detail: detail,
  play: play,
  search: search,
  proxy: proxy,
  sniffer: sniffer,
  isVideo: isVideo,
  DRPY: DRPY
};
  
