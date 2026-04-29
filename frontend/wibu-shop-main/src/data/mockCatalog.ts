export interface MockCategoryItem {
  Id: number;
  Name: string;
  Slug: string;
  Description: string;
}

export interface MockSupplierItem {
  Id: number;
  Name: string;
  ContactInfo: string;
}

export interface MockBranchItem {
  Id: number;
  Name: string;
  Location: string;
}

export interface MockStockItem {
  BranchId: number;
  BranchName: string;
  Location: string;
  VariantId: number;
  VariantName: string;
  Stock: number;
}

export interface MockVariantItem {
  Id: number;
  Name: string;
  Sku: string;
  Price: number;
  OriginalPrice: number;
  ImageUrl: string;
}

export interface MockProductDetailItem {
  Id: number;
  Name: string;
  Slug: string;
  Description: string;
  Status: 'active';
  CategoryName: string;
  Category: {
    Id: number;
    Name: string;
    Slug: string;
  };
  SupplierName: string;
  Supplier: {
    Id: number;
    Name: string;
  };
  Price: number;
  OriginalPrice: number;
  ImageUrl: string;
  Images: string[];
  Rating: number;
  ReviewCount: number;
  SoldCount: number;
  Variants: MockVariantItem[];
}

const image1 = 'https://images.unsplash.com/photo-1618336753974-aae8e04506aa?q=80&w=1200';
const image2 = 'https://images.unsplash.com/photo-1608889175123-8ee362201f81?q=80&w=1200';
const image3 = 'https://images.unsplash.com/photo-1612036782180-6f0b6cd846fe?q=80&w=1200';
const image4 = 'https://images.unsplash.com/photo-1559131397-f94da358f7ca?q=80&w=1200';

export const mockCategories: MockCategoryItem[] = [
  { Id: 1, Name: 'Figures', Slug: 'figures', Description: 'Mô hình nhân vật anime cao cấp' },
  { Id: 2, Name: 'Nendoroid', Slug: 'nendoroids', Description: 'Mô hình chibi dễ thương' },
  { Id: 3, Name: 'Plushies', Slug: 'plushies', Description: 'Gấu bông nhân vật anime' },
  { Id: 4, Name: 'Figma', Slug: 'figma', Description: 'Mô hình có khớp chuyển động' },
];

export const mockSuppliers: MockSupplierItem[] = [
  { Id: 1, Name: 'eStream', ContactInfo: 'tokyo@estream.jp' },
  { Id: 2, Name: 'Good Smile Company', ContactInfo: 'support@goodsmile.jp' },
  { Id: 3, Name: 'Kotobukiya', ContactInfo: 'sales@kotobukiya.co.jp' },
];

export const mockBranches: MockBranchItem[] = [
  { Id: 1, Name: 'Chi nhánh Tokyo', Location: 'Tokyo, Japan' },
  { Id: 2, Name: 'Chi nhánh Osaka', Location: 'Osaka, Japan' },
  { Id: 3, Name: 'Chi nhánh Hà Nội', Location: 'Hà Nội, Việt Nam' },
];

export const mockProducts: MockProductDetailItem[] = [
  {
    Id: 101,
    Name: 'Gojo Satoru - Limited Ver',
    Slug: 'gojo-satoru-limited-ver',
    Description: 'Phiên bản giới hạn với hiệu ứng năng lượng xanh nổi bật.',
    Status: 'active',
    CategoryName: 'Nendoroid',
    Category: { Id: 2, Name: 'Nendoroid', Slug: 'nendoroids' },
    SupplierName: 'Good Smile Company',
    Supplier: { Id: 2, Name: 'Good Smile Company' },
    Price: 1250000,
    OriginalPrice: 1490000,
    ImageUrl: image2,
    Images: [image2, image1],
    Rating: 4.9,
    ReviewCount: 256,
    SoldCount: 1840,
    Variants: [
      { Id: 1001, Name: 'Standard', Sku: 'GOJO-STD', Price: 1250000, OriginalPrice: 1490000, ImageUrl: image2 },
      { Id: 1002, Name: 'DX Edition', Sku: 'GOJO-DX', Price: 1680000, OriginalPrice: 1890000, ImageUrl: image1 },
    ],
  },
  {
    Id: 102,
    Name: 'Nezuko Kamado Figure',
    Slug: 'nezuko-kamado-figure',
    Description: 'Mẫu figure Nezuko với chất liệu sơn tỉ mỉ và chân đế trong suốt.',
    Status: 'active',
    CategoryName: 'Figures',
    Category: { Id: 1, Name: 'Figures', Slug: 'figures' },
    SupplierName: 'eStream',
    Supplier: { Id: 1, Name: 'eStream' },
    Price: 2450000,
    OriginalPrice: 2790000,
    ImageUrl: image1,
    Images: [image1, image3],
    Rating: 4.8,
    ReviewCount: 182,
    SoldCount: 1120,
    Variants: [
      { Id: 2001, Name: '1/8 Scale', Sku: 'NEZ-18', Price: 2450000, OriginalPrice: 2790000, ImageUrl: image1 },
      { Id: 2002, Name: 'Premium Ver', Sku: 'NEZ-PREM', Price: 2890000, OriginalPrice: 3190000, ImageUrl: image3 },
    ],
  },
  {
    Id: 103,
    Name: 'Rem Crystal Dress',
    Slug: 'rem-crystal-dress',
    Description: 'Thiết kế váy pha lê sang trọng với màu xanh đặc trưng của Rem.',
    Status: 'active',
    CategoryName: 'Figures',
    Category: { Id: 1, Name: 'Figures', Slug: 'figures' },
    SupplierName: 'Kotobukiya',
    Supplier: { Id: 3, Name: 'Kotobukiya' },
    Price: 3890000,
    OriginalPrice: 4290000,
    ImageUrl: image3,
    Images: [image3, image1],
    Rating: 4.7,
    ReviewCount: 143,
    SoldCount: 760,
    Variants: [
      { Id: 3001, Name: 'Crystal Dress', Sku: 'REM-CRYSTAL', Price: 3890000, OriginalPrice: 4290000, ImageUrl: image3 },
    ],
  },
  {
    Id: 104,
    Name: 'Pikachu Plushie XL',
    Slug: 'pikachu-plushie-xl',
    Description: 'Plushie cỡ lớn với lớp vải mềm và form dáng tròn đáng yêu.',
    Status: 'active',
    CategoryName: 'Plushies',
    Category: { Id: 3, Name: 'Plushies', Slug: 'plushies' },
    SupplierName: 'Good Smile Company',
    Supplier: { Id: 2, Name: 'Good Smile Company' },
    Price: 890000,
    OriginalPrice: 990000,
    ImageUrl: image4,
    Images: [image4],
    Rating: 4.6,
    ReviewCount: 98,
    SoldCount: 520,
    Variants: [
      { Id: 4001, Name: 'XL Size', Sku: 'PIKA-XL', Price: 890000, OriginalPrice: 990000, ImageUrl: image4 },
    ],
  },
];

export const mockProductsList = mockProducts.map(product => ({
  Id: product.Id,
  Name: product.Name,
  Slug: product.Slug,
  Description: product.Description,
  CategoryId: product.Category.Id,
  CategoryName: product.CategoryName,
  SupplierId: product.Supplier.Id,
  SupplierName: product.SupplierName,
  Price: product.Price,
  OriginalPrice: product.OriginalPrice,
  ImageUrl: product.ImageUrl,
}));

export const mockStocks: MockStockItem[] = [
  { BranchId: 1, BranchName: 'Chi nhánh Tokyo', Location: 'Tokyo, Japan', VariantId: 1001, VariantName: 'Standard', Stock: 12 },
  { BranchId: 2, BranchName: 'Chi nhánh Osaka', Location: 'Osaka, Japan', VariantId: 1001, VariantName: 'Standard', Stock: 5 },
  { BranchId: 3, BranchName: 'Chi nhánh Hà Nội', Location: 'Hà Nội, Việt Nam', VariantId: 1001, VariantName: 'Standard', Stock: 3 },
  { BranchId: 1, BranchName: 'Chi nhánh Tokyo', Location: 'Tokyo, Japan', VariantId: 2001, VariantName: '1/8 Scale', Stock: 7 },
  { BranchId: 2, BranchName: 'Chi nhánh Osaka', Location: 'Osaka, Japan', VariantId: 2001, VariantName: '1/8 Scale', Stock: 0 },
  { BranchId: 3, BranchName: 'Chi nhánh Hà Nội', Location: 'Hà Nội, Việt Nam', VariantId: 2001, VariantName: '1/8 Scale', Stock: 4 },
  { BranchId: 1, BranchName: 'Chi nhánh Tokyo', Location: 'Tokyo, Japan', VariantId: 3001, VariantName: 'Crystal Dress', Stock: 6 },
  { BranchId: 2, BranchName: 'Chi nhánh Osaka', Location: 'Osaka, Japan', VariantId: 3001, VariantName: 'Crystal Dress', Stock: 2 },
  { BranchId: 3, BranchName: 'Chi nhánh Hà Nội', Location: 'Hà Nội, Việt Nam', VariantId: 3001, VariantName: 'Crystal Dress', Stock: 1 },
  { BranchId: 1, BranchName: 'Chi nhánh Tokyo', Location: 'Tokyo, Japan', VariantId: 4001, VariantName: 'XL Size', Stock: 9 },
  { BranchId: 3, BranchName: 'Chi nhánh Hà Nội', Location: 'Hà Nội, Việt Nam', VariantId: 4001, VariantName: 'XL Size', Stock: 2 },
];

export const cloneMockList = () => mockProductsList.map(product => ({ ...product }));

export const cloneMockProductById = (id: number) => {
  const product = mockProducts.find(item => item.Id === id);
  return product ? structuredClone(product) : null;
};

export const getMockProductsByCategory = (categoryId: number) =>
  mockProductsList.filter(product => product.CategoryId === categoryId).map(product => ({ ...product }));

export const searchMockProducts = (keyword: string) => {
  const normalized = keyword.trim().toLowerCase();
  if (!normalized) return [];

  return mockProductsList.filter(product =>
    [product.Name, product.CategoryName, product.Description]
      .some(value => value?.toLowerCase().includes(normalized))
  ).map(product => ({ ...product }));
};

export const getMockStocksByVariant = (variantId: number) =>
  mockStocks.filter(stock => stock.VariantId === variantId).map(stock => ({ ...stock }));

export const getMockStocksByProduct = (productId: number) => {
  const product = mockProducts.find(item => item.Id === productId);
  if (!product) return [];

  const variantIds = product.Variants.map(variant => variant.Id);
  return mockStocks.filter(stock => variantIds.includes(stock.VariantId)).map(stock => ({ ...stock }));
};