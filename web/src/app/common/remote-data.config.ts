import { Injector } from "@angular/core";
import { RemoteDataType, ExtEnumData } from 'calico';

export const EXT_ENUMS: RemoteDataType<ExtEnumData> = {
  key: 'extEnums',
  apiPath: 'endpoint/system/ext_enum',
  apiForm: apiFormForExtEnums,
  transform: transformForExtEnums,
  expired: expiredForExtEnums,
};
export function apiFormForExtEnums(injector: Injector): any {
  return {keys: ['familyType', 'sex']};
}
export function transformForExtEnums(rawData: any): ExtEnumData {
  return rawData;
}
export function expiredForExtEnums(rawData: any): boolean {
  return false;
}

