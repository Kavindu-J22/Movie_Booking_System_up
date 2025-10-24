import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Review, ReviewRequest, ReviewUpdateRequest, MovieRating, ApiResponse } from '../../types';
import api from '../../config/api';

interface ReviewState {
  reviews: Review[];
  movieRating: MovieRating | null;
  isLoading: boolean;
  isSubmitting: boolean;
  error: string | null;
}

const initialState: ReviewState = {
  reviews: [],
  movieRating: null,
  isLoading: false,
  isSubmitting: false,
  error: null,
};

// Async thunks
export const fetchMovieReviews = createAsyncThunk(
  'reviews/fetchMovieReviews',
  async (movieId: string, { rejectWithValue }) => {
    try {
      const response = await api.get<ApiResponse<Review[]>>(`/reviews/movie/${movieId}`);
      return response.data.data || [];
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch reviews');
    }
  }
);

export const fetchMovieRating = createAsyncThunk(
  'reviews/fetchMovieRating',
  async (movieId: string, { rejectWithValue }) => {
    try {
      const response = await api.get<ApiResponse<MovieRating>>(`/reviews/movie/${movieId}/rating`);
      return response.data.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch movie rating');
    }
  }
);

export const createReview = createAsyncThunk(
  'reviews/createReview',
  async (reviewData: ReviewRequest, { rejectWithValue }) => {
    try {
      const response = await api.post<ApiResponse<Review>>('/reviews', reviewData);
      return response.data.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to create review');
    }
  }
);

export const updateReview = createAsyncThunk(
  'reviews/updateReview',
  async ({ reviewId, reviewData }: { reviewId: string; reviewData: ReviewUpdateRequest }, { rejectWithValue }) => {
    try {
      const response = await api.put<ApiResponse<Review>>(`/reviews/${reviewId}`, reviewData);
      return response.data.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to update review');
    }
  }
);

export const deleteReview = createAsyncThunk(
  'reviews/deleteReview',
  async (reviewId: string, { rejectWithValue }) => {
    try {
      await api.delete(`/reviews/${reviewId}`);
      return reviewId;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to delete review');
    }
  }
);

const reviewSlice = createSlice({
  name: 'reviews',
  initialState,
  reducers: {
    clearReviews: (state) => {
      state.reviews = [];
      state.movieRating = null;
      state.error = null;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch movie reviews
      .addCase(fetchMovieReviews.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchMovieReviews.fulfilled, (state, action) => {
        state.isLoading = false;
        state.reviews = action.payload || [];
      })
      .addCase(fetchMovieReviews.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })

      // Fetch movie rating
      .addCase(fetchMovieRating.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchMovieRating.fulfilled, (state, action) => {
        state.isLoading = false;
        state.movieRating = action.payload || null;
      })
      .addCase(fetchMovieRating.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      
      // Create review
      .addCase(createReview.pending, (state) => {
        state.isSubmitting = true;
        state.error = null;
      })
      .addCase(createReview.fulfilled, (state, action) => {
        state.isSubmitting = false;
        if (action.payload) {
          state.reviews.unshift(action.payload);
        }
      })
      .addCase(createReview.rejected, (state, action) => {
        state.isSubmitting = false;
        state.error = action.payload as string;
      })

      // Update review
      .addCase(updateReview.pending, (state) => {
        state.isSubmitting = true;
        state.error = null;
      })
      .addCase(updateReview.fulfilled, (state, action) => {
        state.isSubmitting = false;
        if (action.payload) {
          const index = state.reviews.findIndex(review => review.id === action.payload.id);
          if (index !== -1) {
            state.reviews[index] = action.payload;
          }
        }
      })
      .addCase(updateReview.rejected, (state, action) => {
        state.isSubmitting = false;
        state.error = action.payload as string;
      })
      
      // Delete review
      .addCase(deleteReview.pending, (state) => {
        state.isSubmitting = true;
        state.error = null;
      })
      .addCase(deleteReview.fulfilled, (state, action: PayloadAction<string>) => {
        state.isSubmitting = false;
        state.reviews = state.reviews.filter(review => review.id !== action.payload);
      })
      .addCase(deleteReview.rejected, (state, action) => {
        state.isSubmitting = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearReviews, clearError } = reviewSlice.actions;
export default reviewSlice.reducer;
