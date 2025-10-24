import React, { useEffect } from 'react';
import {
  Box,
  Typography,
  CircularProgress,
  Alert,
  Divider,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import { fetchMovieReviews, fetchMovieRating } from '../../store/slices/reviewSlice';
import ReviewItem from './ReviewItem';
import ReviewForm from './ReviewForm';
import RatingOverview from './RatingOverview';

interface ReviewListProps {
  movieId: string;
}

const ReviewList: React.FC<ReviewListProps> = ({ movieId }) => {
  const dispatch = useDispatch();
  const { reviews, isLoading, error } = useSelector((state: RootState) => state.reviews);

  useEffect(() => {
    if (movieId) {
      dispatch(fetchMovieReviews(movieId) as any);
      dispatch(fetchMovieRating(movieId) as any);
    }
  }, [movieId, dispatch]);

  const handleReviewSubmitted = () => {
    // Refresh reviews and rating after submission
    dispatch(fetchMovieReviews(movieId) as any);
    dispatch(fetchMovieRating(movieId) as any);
  };

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" py={4}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" component="h2" gutterBottom sx={{ fontWeight: 'bold' }}>
        Ratings & Reviews
      </Typography>

      {/* Rating Overview */}
      <RatingOverview movieId={movieId} />

      <Divider sx={{ my: 3 }} />

      {/* Review Form */}
      <ReviewForm movieId={movieId} onReviewSubmitted={handleReviewSubmitted} />

      {/* Reviews List */}
      <Box>
        <Typography variant="h6" gutterBottom>
          Customer Reviews ({reviews.length})
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        {reviews.length === 0 ? (
          <Typography variant="body1" color="text.secondary" sx={{ py: 4, textAlign: 'center' }}>
            No reviews yet. Be the first to review this movie!
          </Typography>
        ) : (
          <Box>
            {reviews.map((review) => (
              <ReviewItem key={review.id} review={review} />
            ))}
          </Box>
        )}
      </Box>
    </Box>
  );
};

export default ReviewList;
