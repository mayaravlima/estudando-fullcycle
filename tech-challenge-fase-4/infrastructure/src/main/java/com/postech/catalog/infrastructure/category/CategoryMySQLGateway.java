package com.postech.catalog.infrastructure.category;

import com.postech.catalog.domain.catagory.Category;
import com.postech.catalog.domain.catagory.CategoryGateway;
import com.postech.catalog.domain.catagory.CategoryID;
import com.postech.catalog.domain.catagory.CategorySearchQuery;
import com.postech.catalog.domain.pagination.Pagination;
import com.postech.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.postech.catalog.infrastructure.category.persistence.CategoryRepository;
import com.postech.catalog.infrastructure.utils.SpecificationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.postech.catalog.infrastructure.utils.SpecificationUtils.like;

@Service
@AllArgsConstructor
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(final Category category) {
        return save(category);
    }

    @Override
    public void deleteById(CategoryID id) {
        final String idValue = id.getValue();
        if (this.categoryRepository.existsById(idValue)) {
            this.categoryRepository.deleteById(idValue);
        }
    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return this.categoryRepository.findById(id.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(Category category) {
        return save(category);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str ->
                        SpecificationUtils
                                .<CategoryJpaEntity>like("name", str)
                                .or(like("description", str))
                ).orElse(null);

        final var pageResult =
                this.categoryRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(final Category category) {
        return this.categoryRepository
                .save(CategoryJpaEntity.from(category))
                .toAggregate();
    }


}
