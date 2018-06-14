export class DataFile {
    constructor(
        public id?: string,
        public truncateData?: boolean,
        public dataFileContentType?: string,
        public dataFile?: any,
        public createdAt?: any
    ) {
        this.truncateData = false;
    }
}
