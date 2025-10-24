import React, { useState } from 'react';
import {
  Box,
  Typography,
  TextField,
  Button,
  Rating,
  Paper,
  Alert,
  CircularProgress,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import { createReview } from '../../store/slices/reviewSlice';
import { ReviewRequest } from '../../types';

interface ReviewFormProps {
  movieId: string;
  onReviewSubmitted?: () => void;
}

const ReviewForm: React.FC<ReviewFormProps> = ({ movieId, onReviewSubmitted }) => {
  const dispatch = useDispatch();
  const { isSubmitting, error } = useSelector((state: RootState) => state.reviews);
  const { user } = useSelector((state: RootState) => state.auth);

  const [formData, setFormData] = useState({
    email: user?.email || '',
    reviewerName: user ? `${user.firstName} ${user.lastName}` : '',
    rating: 0,
    reviewText: '',
  });

  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const validateEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validateForm = () => {
    const newErrors: { [key: string]: string } = {};

    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!validateEmail(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (!formData.reviewerName.trim()) {
      newErrors.reviewerName = 'Name is required';
    } else if (formData.reviewerName.trim().length < 2) {
      newErrors.reviewerName = 'Name must be at least 2 characters';
    }

    if (formData.rating === 0) {
      newErrors.rating = 'Please select a rating';
    }

    if (!formData.reviewText.trim()) {
      newErrors.reviewText = 'Review text is required';
    } else if (formData.reviewText.trim().length < 10) {
      newErrors.reviewText = 'Review must be at least 10 characters';
    } else if (formData.reviewText.trim().length > 1000) {
      newErrors.reviewText = 'Review must be less than 1000 characters';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    const reviewData: ReviewRequest = {
      movieId,
      email: formData.email.trim(),
      reviewerName: formData.reviewerName.trim(),
      rating: formData.rating,
      reviewText: formData.reviewText.trim(),
    };

    try {
      await dispatch(createReview(reviewData) as any);
      // Reset form
      setFormData({
        email: user?.email || '',
        reviewerName: user ? `${user.firstName} ${user.lastName}` : '',
        rating: 0,
        reviewText: '',
      });
      setErrors({});
      if (onReviewSubmitted) {
        onReviewSubmitted();
      }
    } catch (error) {
      // Error is handled by Redux
    }
  };

  const handleInputChange = (field: string, value: any) => {
    setFormData(prev => ({ ...prev, [field]: value }));
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: '' }));
    }
  };

  return (
    <Paper sx={{ p: 3, mb: 3 }}>
      <Typography variant="h6" gutterBottom>
        Write a Review
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Box component="form" onSubmit={handleSubmit}>
        <Box sx={{ mb: 3 }}>
          <TextField
            fullWidth
            label="Email"
            type="email"
            value={formData.email}
            onChange={(e) => handleInputChange('email', e.target.value)}
            error={!!errors.email}
            helperText={errors.email}
            disabled={!!user?.email}
            required
          />
        </Box>

        <Box sx={{ mb: 3 }}>
          <TextField
            fullWidth
            label="Your Name"
            value={formData.reviewerName}
            onChange={(e) => handleInputChange('reviewerName', e.target.value)}
            error={!!errors.reviewerName}
            helperText={errors.reviewerName}
            required
          />
        </Box>

        <Box sx={{ mb: 3 }}>
          <Typography component="legend" gutterBottom>
            Rating *
          </Typography>
          <Rating
            value={formData.rating}
            onChange={(_, newValue) => handleInputChange('rating', newValue || 0)}
            size="large"
          />
          {errors.rating && (
            <Typography variant="caption" color="error" display="block">
              {errors.rating}
            </Typography>
          )}
        </Box>

        <Box sx={{ mb: 3 }}>
          <TextField
            fullWidth
            label="Your Review"
            multiline
            rows={4}
            value={formData.reviewText}
            onChange={(e) => handleInputChange('reviewText', e.target.value)}
            error={!!errors.reviewText}
            helperText={errors.reviewText || `${formData.reviewText.length}/1000 characters`}
            required
          />
        </Box>

        <Button
          type="submit"
          variant="contained"
          size="large"
          disabled={isSubmitting}
          startIcon={isSubmitting ? <CircularProgress size={20} /> : null}
        >
          {isSubmitting ? 'Submitting...' : 'Submit Review'}
        </Button>
      </Box>
    </Paper>
  );
};

export default ReviewForm;
