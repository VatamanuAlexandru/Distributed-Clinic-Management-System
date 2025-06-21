export interface TestParameterResult {
  id?: string;
  parameterName: string;
  value: string;
  unit: string;
  referenceRange: string;
  notes?: string;
}

export enum TestCategory {
  BLOOD_TEST = 'BLOOD_TEST',
  IMAGING = 'IMAGING',
  MICROBIOLOGY = 'MICROBIOLOGY',
  HISTOPATHOLOGY = 'HISTOPATHOLOGY',
  GENETIC = 'GENETIC',
  OTHER = 'OTHER'
}
export const TestCategoryLabels: Record<TestCategory, string> = {
  [TestCategory.BLOOD_TEST]: 'Analiză sânge',
  [TestCategory.IMAGING]: 'Imagistică',
  [TestCategory.MICROBIOLOGY]: 'Microbiologie',
  [TestCategory.HISTOPATHOLOGY]: 'Histopatologie',
  [TestCategory.GENETIC]: 'Genetic',
  [TestCategory.OTHER]: 'Altă categorie'
};


export interface MedicalTest {
  id?: string;
  testName: string;
  category: TestCategory;
  result: string;
  testDate: string;          // ISO string
  parameterResults: TestParameterResult[];
}
