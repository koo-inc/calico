import {Injectable} from '@angular/core';

import {String as s} from 'sugar';

@Injectable()
export class SerializeService {
  serialize(obj: any): string {
    return s.encodeBase64(JSON.stringify(obj));
  }
  deserialize<T>(str: string): T {
    try {
      return JSON.parse(s.decodeBase64(str)) as T;
    } catch (e) {
      return null;
    }
  }
}

