import { Injectable, Injector } from "@angular/core";
import { RemoteDataType, RemoteDataService, ExtEnumData, ExtEnumDataProvider} from 'calico';

export const EXT_ENUMS: RemoteDataType<ExtEnumData> = {
  key: 'extEnums',
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

@Injectable()
export class AppExtEnumDataProvider implements ExtEnumDataProvider {
  constructor(
    private remoteDataService: RemoteDataService,
  ){};

  getAll(): ExtEnumData {
    return this.remoteDataService.get(EXT_ENUMS);
  }
}
