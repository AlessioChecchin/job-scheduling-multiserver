package entries;

import config.CategoryConfig;

public interface Categorizable
{
    void setCategory(CategoryConfig config);
    CategoryConfig getCategory();
}
