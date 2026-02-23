package com.danyeon.newsreader.feature.news.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.danyeon.newsreader.core.network.model.Category
import com.danyeon.newsreader.core.ui.TopBar
import com.danyeon.newsreader.core.util.WebNavigationHelper
import com.danyeon.newsreader.feature.news.data.entity.NewsArticle
import com.danyeon.newsreader.feature.news.presentation.state.NewsState
import com.danyeon.newsreader.feature.news.presentation.viewmodel.NewsViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun MainScreen(newsViewModel: NewsViewModel = hiltViewModel()) {

    val listState = rememberLazyListState()
    val newsState by newsViewModel.state.collectAsState()
    val selectedCategory by newsViewModel.selectedCategory.collectAsState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem >= totalItems - 5 && totalItems > 0
        }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                newsViewModel.loadNextPage()
            }
    }

    when (newsState) {
        NewsState.Idle -> {}
        NewsState.Loading -> {}
        is NewsState.Success -> {
            MainScreenContent(
                (newsState as NewsState.Success).data,
                listState = listState,
                selectedCategory = selectedCategory,
                onCategorySelected = { newsViewModel.updateCategory(it) }
            )
        }

        is NewsState.Error -> {}
    }
}

@Composable
private fun MainScreenContent(
    articles: List<NewsArticle>,
    selectedCategory: Category,
    listState: LazyListState,
    onCategorySelected: (Category) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(
                state = listState
            ) {
                item {
                    AnimatedFilterBar(
                        Category.entries,
                        selectedCategory = selectedCategory
                    ) {
                        onCategorySelected(it)
                    }
                }

                items(articles) { article ->
                    CardNews(article) {
                        WebNavigationHelper.openCustomTab(context, article.url)
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedFilterBar(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory

            val containerColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                label = "ContainerColor"
            )

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    AnimatedVisibility(visible = isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = containerColor
                ),
                border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
private fun CardNews(
    article: NewsArticle,
    onCardClick: (String) -> Unit,
) {
    // 디자인 철학: ElevatedCard를 사용하여 배경과 분리된 입체감을 주고,
    // 내장된 클릭 리플 효과를 활용합니다.
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            // MD3의 기본 쉐이프를 따르며, 클릭 시 리플이 둥근 모서리 밖으로 나가지 않게 clip합니다.
            .clip(CardDefaults.shape)
            .clickable { onCardClick(article.url) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // 1. 시각 자료 (Image Placeholder)
            // 실제 앱에서는 AsyncImage(Coil 등)를 사용해야 합니다.
            // 16:9 비율을 유지하여 몰입감을 줍니다.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                // 이미지 로딩 전/실패 시 보여줄 아이콘이나 텍스트
                Text(
                    text = "News Image",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 2. 콘텐츠 영역 (Padding 적용)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // 내부 콘텐츠에 충분한 여백 제공
            ) {
                // 3. 메타 정보 Row (Level Badge + Theme/Category)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Level Badge: 난이도는 별도의 칩 형태로 강조하여 인지하기 쉽게 합니다.
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Text(
                            text = article.level.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Theme • Category: 보조 정보는 합쳐서 보여주고, 색상을 낮춰 타이틀을 방해하지 않습니다.
                    Text(
                        text = "${article.theme} • ${article.category.name}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // 요소 간 간격

                // 4. 타이틀 (가장 중요한 정보)
                Text(
                    text = article.title,
                    // titleMedium 혹은 상황에 따라 titleLarge 사용
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3, // 너무 길어지면 말줄임표 처리
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview
@Composable
private fun MainScreenPreview() {
    MainScreenContent(
        listOf(), Category.ALL, rememberLazyListState(),
    ) {}
}