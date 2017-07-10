import { Injectable, Injector } from "@angular/core";
import { RemoteDataType, RemoteDataService, ExtEnumData, ExtEnum, ExtEnumDataProvider} from 'calico';

export const EXT_ENUM: RemoteDataType<ExtEnumData> = {
  key: 'ExtEnum',
  apiPath: 'endpoint/system/get_ext_enums',
  transform: transformForExtEnums,
  expired: expiredForExtEnums,
};
export function transformForExtEnums(rawData: any): ExtEnumData {
  return rawData;
}
export function expiredForExtEnums(rawData: any): boolean {
  return false;
}

export const FAMILY_TYPE: RemoteDataType<ExtEnum[]> = {
  key: 'FamilyType',
  apiPath: 'endpoint/system/get_ext_enums',
  transform: transformForFamilyTypes,
};
export function transformForFamilyTypes(rawData: any): ExtEnum[] {
  return rawData['FamilyType'];
}

export const NOT_ENSURED_FAMILY_TYPE: RemoteDataType<ExtEnum[]> = {
  key: 'NotEnsuredFamilyType',
  apiPath: 'endpoint/system/get_ext_enums',
  transform: transformForFamilyTypes,
  expired: expiredForNotEnsuredFamilyTypes,
  localStorageCahche: false,
  ensure: false,
};
export function expiredForNotEnsuredFamilyTypes(rawData: any): boolean {
  return true;
}

@Injectable()
export class AppExtEnumDataProvider implements ExtEnumDataProvider {
  constructor(
    private remoteDataService: RemoteDataService,
  ){};

  getAll(): ExtEnumData {
    return this.remoteDataService.get(EXT_ENUM);
  }
}
